package com.blueiobase.api.android.chiplayoutmanager.main

import android.content.Context
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import androidx.annotation.VisibleForTesting
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.anchor.contract.IAnchorFactory
import com.blueiobase.api.android.chiplayoutmanager.annotation.DeviceOrientation
import com.blueiobase.api.android.chiplayoutmanager.annotation.Orientation
import com.blueiobase.api.android.chiplayoutmanager.annotation.RowStrategy
import com.blueiobase.api.android.chiplayoutmanager.annotation.SpanLayoutChildGravity
import com.blueiobase.api.android.chiplayoutmanager.cache.ViewCacheFactory
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IChipsLayoutManagerContract
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IDisappearingViewsManager
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IScrollingController
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IStateHolder
import com.blueiobase.api.android.chiplayoutmanager.gravity.CenterChildGravity
import com.blueiobase.api.android.chiplayoutmanager.gravity.CustomGravityResolver
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IChildGravityResolver
import com.blueiobase.api.android.chiplayoutmanager.layouter.*
import com.blueiobase.api.android.chiplayoutmanager.breaker.EmptyRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.ICriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.InfiniteCriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ICanvas
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IMeasureSupporter
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IStateFactory
import com.blueiobase.api.android.chiplayoutmanager.placer.PlacerFactory
import com.blueiobase.api.android.chiplayoutmanager.main.model.ParcelableContainer
import com.blueiobase.api.android.chiplayoutmanager.util.AssertionUtils.assertNotNull
import com.blueiobase.api.android.chiplayoutmanager.util.LayoutManagerUtil.requestLayoutWithAnimations
import com.blueiobase.api.android.chiplayoutmanager.util.log.IFillLogger
import com.blueiobase.api.android.chiplayoutmanager.util.log.Log
import com.blueiobase.api.android.chiplayoutmanager.util.log.LogSwitcherFactory
import com.blueiobase.api.android.chiplayoutmanager.util.log.LoggerFactory
import com.blueiobase.api.android.chiplayoutmanager.util.testing.EmptySpy
import com.blueiobase.api.android.chiplayoutmanager.util.testing.ISpy
import java.util.*


class ChipLayoutManager(context: Context)
    : RecyclerView.LayoutManager(), IChipsLayoutManagerContract, IStateHolder,
    ScrollingController.IScrollerListener {

    companion object {
        ///////////////////////////////////////////////////////////////////////////
        // orientation types
        ///////////////////////////////////////////////////////////////////////////
        const val HORIZONTAL = 1
        const val VERTICAL = 2

        ///////////////////////////////////////////////////////////////////////////
        // row strategy types
        ///////////////////////////////////////////////////////////////////////////
        /**
         * Constant denoting the default [RowStrategy] where unused space is appended to the last view on the row.
         */
        const val STRATEGY_DEFAULT = 1

        /**
         * Constant denoting [RowStrategy] where space is evenly distributed among views.
         */
        const val STRATEGY_FILL_VIEW = 2

        /**
         * Constant denoting [RowStrategy] where space is evenly distributed among views.
         * First [View] is appended to the start (left) of parent and End [View] is appended to the end (right) of parent.
         */
        const val STRATEGY_FILL_SPACE = 4

        /**
         * Constant denoting [RowStrategy] where space is evenly distributed among views.
         * Every [View] is placed at the center of the available drawing [canvas][ICanvas].
         */
        const val STRATEGY_CENTER = 5

        /**
         * Constant denoting [RowStrategy] where every [View] is clustered and placed at the center of the available drawing [canvas][ICanvas].
         */
        const val STRATEGY_CENTER_DENSE = 6

        ///////////////////////////////////////////////////////////////////////////
        // inner constants
        ///////////////////////////////////////////////////////////////////////////
        private val TAG = ChipLayoutManager::class.java.simpleName
        private const val INT_ROW_SIZE_APPROXIMATELY_FOR_CACHE = 10
        private const val APPROXIMATE_ADDITIONAL_ROWS_COUNT = 5

        /**
         * coefficient to support fast scrolling, caching views only for one row may not be enough
         */
        private const val FAST_SCROLLING_COEFFICIENT = 2f

        fun newBuilder(context: Context?): Builder {
            requireNotNull(context) { "you have passed null context to builder" }
            return ChipLayoutManager(context).StrategyBuilder()
        }
    }

    /**
     * The canvas upon which views are drawn on.
     */
    var canvas: ICanvas? = null
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) get

    private var disappearingViewsManager: IDisappearingViewsManager? = null

    /** iterable over views added to RecyclerView  */
    private val childViews = ChildViewsIterable(this)

    private val childViewPositions = SparseArray<View>()

    ///////////////////////////////////////////////////////////////////////////
    // contract parameters
    ///////////////////////////////////////////////////////////////////////////
    /** determine gravity of child inside row */
    var childGravityResolver: IChildGravityResolver? = null

    private var isScrollingEnabledContract = true

    /** strict restriction of max count of views in particular row  */
    private var maxViewsInRow = 0

    /** determines whether LM should break row from view position  */
    private var rowBreaker: IRowBreaker = EmptyRowBreaker()

    //--- end contract parameters
    /** layoutOrientation of layout. Could have HORIZONTAL or VERTICAL style  */
    @Orientation
    private var layoutOrientation = HORIZONTAL

    @RowStrategy
    private var rowStrategy = STRATEGY_DEFAULT

    var isStrategyAppliedWithLastRow = false
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) get

    /** @see setSmoothScrollbarEnabled
     */
    private var isSmoothScrollbarEnabled = false

    ///////////////////////////////////////////////////////////////////////////
    // cache
    ///////////////////////////////////////////////////////////////////////////
    /** store positions of placed view to know when LM should break row while moving back
     * this cache mostly needed to place views when scrolling down to the same places, where they have been previously  */
    val viewPositionsStorage = ViewCacheFactory(this).createCacheStorage()
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) get

    /**
     * when scrolling reached this position [ChipLayoutManager] is able to restore items layout according to cached items with positions above.
     * That layout would exactly correspond to current item view situation
     */
    private var cacheNormalizationPosition: Int? = null

    /**
     * store detached views to probably reattach it if them still visible.
     * Used while scrolling
     */
    private val viewCache = SparseArray<View>()

    /**
     * storing state due layoutOrientation changes
     */
    private var container = ParcelableContainer()

    ///////////////////////////////////////////////////////////////////////////
    // loggers
    ///////////////////////////////////////////////////////////////////////////
    val loggerFactory = LoggerFactory()
    private var logger: IFillLogger? = loggerFactory.getFillLogger(viewCache)

    //--- end loggers
    /**
     * is layout in RTL mode. Variable needed to detect mode changes
     */
    private var isLayoutRTL = false

    /**
     * current device layoutOrientation
     */
    @DeviceOrientation
    private val orientation = context.resources.configuration.orientation

    ///////////////////////////////////////////////////////////////////////////
    // borders
    ///////////////////////////////////////////////////////////////////////////
    /**
     * stored current anchor view due to scroll state changes
     */
    var anchorView: AnchorViewState? = null
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) get

    ///////////////////////////////////////////////////////////////////////////
    // state-dependent
    ///////////////////////////////////////////////////////////////////////////
    /** factory for state-dependent layouter factories */
    private var stateFactory: IStateFactory? = null

    /** manage auto-measuring  */
    private val measureSupporter: IMeasureSupporter = MeasureSupporter(this)

    /** factory which could retrieve anchorView on which layouting based */
    private var anchorFactory: IAnchorFactory? = null

    /** manage scrolling of layout manager according to current state  */
    private var scrollingController: IScrollingController? = null
    //--- end state-dependent vars

    //--- end state-dependent vars
    /** factory for placers factories */
    private val placerFactory = PlacerFactory(this)

    /** used for testing purposes to spy for [ChipLayoutManager] behaviour  */
    var spy: ISpy = EmptySpy()
        @VisibleForTesting
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) set

    private var isAfterPreLayout = false

    //TODO: Check if isAutoMeasureEnabled is needed
//    override fun isAutoMeasureEnabled() = true

    ///////////////////////////////////////////////////////////////////////////
    // ChipsLayoutManager contract methods
    ///////////////////////////////////////////////////////////////////////////

    /** use it to strictly disable scrolling.
     * If scrolling enabled it would be disabled in case all items fit on the screen  */
    override fun setScrollingEnabledContract(isEnabled: Boolean) {
        isScrollingEnabledContract = isEnabled
    }

    override fun isScrollingEnabledContract() = isScrollingEnabledContract

    /**
     * change max count of row views in runtime
     */
    override fun setMaxViewsInRow(@IntRange(from = 1) maxViewsInRow: Int) {
        require(maxViewsInRow >= 1) { "maxViewsInRow should be positive, but is = $maxViewsInRow" }
        this.maxViewsInRow = maxViewsInRow
        onRuntimeLayoutChanges()
    }

    private fun onRuntimeLayoutChanges() {
        cacheNormalizationPosition = 0
        viewPositionsStorage.purge()
        requestLayoutWithAnimations()
    }

    override fun getMaxViewsInRow(): Int {
        return maxViewsInRow
    }

    override fun getRowBreaker(): IRowBreaker {
        return rowBreaker
    }

    @RowStrategy
    override fun getRowStrategyType(): Int {
        return rowStrategy
    }


    ///////////////////////////////////////////////////////////////////////////
    // builder
    ///////////////////////////////////////////////////////////////////////////
    //create decorator if any other builders would be added
    inner class StrategyBuilder : Builder() {
        /** @param withLastRow true, if row strategy should be applied to last row.
         * @see Builder.setRowStrategy
         */
        fun withLastRow(withLastRow: Boolean): Builder {
            isStrategyAppliedWithLastRow = withLastRow
            return this
        }
    }

    open inner class Builder {

        @SpanLayoutChildGravity
        private var gravity: Int? = null

        /**
         * set vertical gravity in a row for all children. Default = CENTER_VERTICAL
         */
        fun setChildGravity(@SpanLayoutChildGravity gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        /**
         * set gravity resolver in case you need special gravity for items. This method have priority over [.setChildGravity]
         */
        fun setGravityResolver(gravityResolver: IChildGravityResolver): Builder {
            assertNotNull(gravityResolver, "gravity resolver couldn't be null")
            childGravityResolver = gravityResolver
            return this
        }

        /**
         * strictly disable scrolling if needed
         */
        fun setScrollingEnabled(isEnabled: Boolean): Builder {
            setScrollingEnabledContract(isEnabled)
            return this
        }

        /** row strategy for views in completed row.
         * Any row has some space left, where is impossible to place the next view, because that space is too small.
         * But we could distribute that space for available views in that row
         * @param rowStrategy is a mode of distribution left space<br></br>
         * [.STRATEGY_DEFAULT] is used by default. Left space is placed at the end of the row.<br></br>
         * [.STRATEGY_FILL_VIEW] available space is distributed among views<br></br>
         * [.STRATEGY_FILL_SPACE] available space is distributed among spaces between views, start & end views are docked to a nearest border<br></br>
         * [.STRATEGY_CENTER] available space is distributed among spaces between views, start & end spaces included. Views are placed in center of canvas<br></br>
         * [.STRATEGY_CENTER_DENSE] available space is distributed among start & end spaces. Views are placed in center of canvas<br></br>
         * <br></br>
         * In such layouts by default last row isn't considered completed. So strategy isn't applied for last row.<br></br>
         * But you can also enable opposite behaviour.
         * @see StrategyBuilder.withLastRow
         */
        fun setRowStrategy(@RowStrategy rowStrategy: Int): StrategyBuilder {
            this@ChipLayoutManager.rowStrategy = rowStrategy
            return this as StrategyBuilder
        }

        /**
         * set maximum possible count of views in row
         */
        fun setMaxViewsInRow(@IntRange(from = 1) maxViewsInRow: Int): Builder {
            require(maxViewsInRow >= 1) { "maxViewsInRow should be positive, but is = $maxViewsInRow" }
            this@ChipLayoutManager.maxViewsInRow = maxViewsInRow
            return this
        }

        /** @param breaker override to determine whether ChipsLayoutManager should breaks row due to position of view.
         */
        fun setRowBreaker(breaker: IRowBreaker): Builder {
            assertNotNull(breaker, "breaker couldn't be null")
            this@ChipLayoutManager.rowBreaker = breaker
            return this
        }

        /** @param orientation of layout manager. Could be [.HORIZONTAL] or [.VERTICAL]
         * [.HORIZONTAL] by default
         */
        fun setOrientation(@Orientation orientation: Int): Builder {
            if (orientation != HORIZONTAL && orientation != VERTICAL) {
                return this
            }
            this@ChipLayoutManager.layoutOrientation = orientation
            return this
        }

        /**
         * create SpanLayoutManager
         */
        fun build(): ChipLayoutManager {
            // setGravityResolver always have priority
            if (childGravityResolver == null) {
                childGravityResolver = if (gravity != null) {
                    CustomGravityResolver(gravity!!)
                } else {
                    CenterChildGravity()
                }
            }
            stateFactory = if (layoutOrientation == HORIZONTAL) //TODO: Verify that this step is not problematic.
                RowsStateFactory(this@ChipLayoutManager) else ColumnsStateFactory(
                    this@ChipLayoutManager
                )

            stateFactory!!.apply {
                canvas = createCanvas()
                anchorFactory = anchorFactory()
                scrollingController = scrollingController()
                anchorView = anchorFactory!!.createNotFound()
                disappearingViewsManager = DisappearingViewsManager(canvas!!, childViews, this)

            }
            return this@ChipLayoutManager
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    private fun requestLayoutWithAnimations() {
        requestLayoutWithAnimations(this)
    }

    ///////////////////////////////////////////////////////////////////////////
    // instance state
    ///////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    override fun onRestoreInstanceState(state: Parcelable) {
        container = state as ParcelableContainer
        anchorView = container.anchorViewState
        if (orientation != container.orientation) {
            //orientation have been changed, clear anchor rect
            var anchorPos: Int

            anchorView!!.position?.let {
                anchorPos = it
                anchorView = anchorFactory!!.createNotFound()
                anchorView!!.position = anchorPos
            }

        }
        viewPositionsStorage.onRestoreInstanceState(container.getPositionsCache(orientation))
        cacheNormalizationPosition = container.getNormalizationPosition(orientation)
        Log.d(
            TAG,
            "RESTORE. last cache position before cleanup = " + viewPositionsStorage.getLastCachePosition()
        )
        if (cacheNormalizationPosition != null) {
            viewPositionsStorage.purgeCacheFromPosition(cacheNormalizationPosition!!)
        }

        anchorView!!.position?.let {
            viewPositionsStorage.purgeCacheFromPosition(it)
            Log.d(TAG, "RESTORE. anchor position = + $it")
        }

        Log.d(
            TAG,
            "RESTORE. layoutOrientation = $orientation normalizationPos = $cacheNormalizationPosition"
        )
        Log.d(TAG, "RESTORE. last cache position = " + viewPositionsStorage.getLastCachePosition())
    }

    /**
     * {@inheritDoc}
     */
    override fun onSaveInstanceState(): Parcelable {
        container.putAnchorViewState(anchorView!!)
        container.putPositionsCache(orientation, viewPositionsStorage.onSaveInstanceState())
        container.putOrientation(orientation)
        Log.d(TAG, "STORE. last cache position =" + viewPositionsStorage.getLastCachePosition())
        val storedNormalizationPosition =
            if (cacheNormalizationPosition != null) cacheNormalizationPosition!! else viewPositionsStorage.getLastCachePosition()!!
        Log.d(
            TAG,
            "STORE. layoutOrientation = $orientation normalizationPos = $storedNormalizationPosition"
        )
        container.putNormalizationPosition(orientation, storedNormalizationPosition)
        return container
    }

    /**
     * {@inheritDoc}
     */
    override fun supportsPredictiveItemAnimations(): Boolean {
        return true
    }

    ///////////////////////////////////////////////////////////////////////////
    // visible items
    ///////////////////////////////////////////////////////////////////////////
    /** returns count of completely visible views
     * @see .findFirstCompletelyVisibleItemPosition
     * @see .findLastCompletelyVisibleItemPosition
     */
    fun getCompletelyVisibleViewsCount(): Int {
        var visibleViewsCount = 0
        for (child in childViews) {
            if (canvas!!.isFullyVisible(child)) {
                visibleViewsCount++
            }
        }
        return visibleViewsCount
    }

    ///////////////////////////////////////////////////////////////////////////
    // positions contract
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Returns the adapter position of the first visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     * If RecyclerView has item decorators, they will be considered in calculations as well.
     *
     *
     * LayoutManager may pre-cache some views that are not necessarily visible. Those views
     * are ignored in this method.
     *
     * @return The adapter position of the first visible item or [RecyclerView.NO_POSITION] if
     * there aren't any visible items.
     * @see .findFirstCompletelyVisibleItemPosition
     * @see .findLastVisibleItemPosition
     */
    override fun findFirstVisibleItemPosition(): Int {
        return if (childCount == 0) RecyclerView.NO_POSITION else canvas!!.getMinPositionOnScreen()!!
    }

    /**
     * Returns the adapter position of the first fully visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     *
     * @return The adapter position of the first fully visible item or
     * [RecyclerView.NO_POSITION] if there aren't any visible items.
     * @see .findFirstVisibleItemPosition
     * @see .findLastCompletelyVisibleItemPosition
     */
    override fun findFirstCompletelyVisibleItemPosition(): Int {
        for (view in childViews) {
            val rect = canvas!!.getViewRect(view)
            if (!canvas!!.isFullyVisible(rect)) continue
            if (canvas!!.isInside(rect)) {
                return getPosition(view)
            }
        }
        return RecyclerView.NO_POSITION
    }


    /**
     * Returns the adapter position of the last visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     * If RecyclerView has item decorators, they will be considered in calculations as well.
     *
     *
     * LayoutManager may pre-cache some views that are not necessarily visible. Those views
     * are ignored in this method.
     *
     * @return The adapter position of the last visible view or [RecyclerView.NO_POSITION] if
     * there aren't any visible items.
     * @see .findLastCompletelyVisibleItemPosition
     * @see .findFirstVisibleItemPosition
     */
    override fun findLastVisibleItemPosition(): Int {
        return if (childCount == 0) RecyclerView.NO_POSITION else canvas!!.getMaxPositionOnScreen()!!
    }

    /**
     * Returns the adapter position of the last fully visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     *
     * @return The adapter position of the last fully visible view or
     * [RecyclerView.NO_POSITION] if there aren't any visible items.
     * @see .findLastVisibleItemPosition
     * @see .findFirstCompletelyVisibleItemPosition
     */
    override fun findLastCompletelyVisibleItemPosition(): Int {
        for (i in childCount - 1 downTo 0) {
            val view = getChildAt(i)
            val rect = canvas!!.getViewRect(view!!)
            if (!canvas!!.isFullyVisible(rect)) continue
            if (canvas!!.isInside(view)) {
                return getPosition(view)
            }
        }
        return RecyclerView.NO_POSITION
    }

    /** @return child for requested position. Null if that child haven't added to layout manager
     */
    fun getChildWithPosition(position: Int): View? {
        return childViewPositions[position]
    }


    ///////////////////////////////////////////////////////////////////////////
    // orientation
    ///////////////////////////////////////////////////////////////////////////
    /**
     * @return true if RTL mode enabled in RecyclerView
     */
    override fun isLayoutRTL() = layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL

    @Orientation
    override fun layoutOrientation(): Int {
        return layoutOrientation
    }

    ///////////////////////////////////////////////////////////////////////////
    // layouting
    ///////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    override fun getItemCount(): Int {
        //in pre-layouter drawing we need item count with items will be actually deleted to pre-draw appearing items properly
        return super.getItemCount() + disappearingViewsManager!!.getDeletingItemsOnScreenCount()
    }


    /**
     * {@inheritDoc}
     */
    override fun onLayoutChildren(recycler: Recycler?, state: RecyclerView.State) {
        spy.onLayoutChildren(recycler!!, state)
        Log.d(TAG, "onLayoutChildren. State =$state")
        //We have nothing to show for an empty data set but clear any existing views
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            return
        }
        Log.i(
            "onLayoutChildren",
            "isPreLayout = " + state.isPreLayout,
            LogSwitcherFactory.PREDICTIVE_ANIMATIONS
        )
        if (isLayoutRTL() != isLayoutRTL) {
            //if layout direction changed programmatically we should clear anchors
            isLayoutRTL = isLayoutRTL()
            //so detach all views before we start searching for anchor view
            detachAndScrapAttachedViews(recycler)
        }
        calcRecyclerCacheSize(recycler)
        if (state.isPreLayout) {
            //inside pre-layout stage. It is called when item animation reconstruction will be played
            //it is NOT called on layoutOrientation changes
            val additionalLength = disappearingViewsManager!!.calcDisappearingViewsLength(recycler)
            Log.d("LayoutManager", "height =$height", LogSwitcherFactory.PREDICTIVE_ANIMATIONS)
            Log.d(
                "onDeletingHeightCalc",
                "additional height  = $additionalLength",
                LogSwitcherFactory.PREDICTIVE_ANIMATIONS
            )
            anchorView = anchorFactory!!.getAnchor()
            anchorFactory!!.resetRowCoordinates(anchorView!!)
            Log.w(TAG, "anchor state in pre-layout = $anchorView")
            detachAndScrapAttachedViews(recycler)

            //in case removing draw additional rows to show predictive animations for appearing views
            val criteriaFactory = stateFactory!!.createDefaultFinishingCriteriaFactory()
            criteriaFactory.additionalRowCount = APPROXIMATE_ADDITIONAL_ROWS_COUNT
            criteriaFactory.additionalLength = additionalLength
            val layouterFactory = stateFactory!!.createLayouterFactory(
                criteriaFactory,
                placerFactory.createRealPlacerFactory()
            )
            logger!!.onBeforeLayouter(anchorView)
            fill(
                recycler,
                layouterFactory.getBackwardLayouter(anchorView!!),
                layouterFactory.getForwardLayouter(anchorView!!)
            )
            isAfterPreLayout = true
        } else {
            detachAndScrapAttachedViews(recycler)

            //we perform layouting stage from scratch, so cache will be rebuilt soon, we could purge it and avoid unnecessary normalization
            anchorView!!.position?.let {
                if (cacheNormalizationPosition != null && it <= cacheNormalizationPosition!!) {
                    cacheNormalizationPosition = null
                }
                viewPositionsStorage.purgeCacheFromPosition(it)

            }

            /* In case some moving views
             * we should place it at layout to support predictive animations
             * we can't place all possible moves on theirs real place, because concrete layout position of particular view depends on placing of previous views
             * and there could be moving from 0 position to 10k. But it is preferably to place nearest moved view to real positions to make moving more natural
             * like moving from 0 position to 15 for example, where user could scroll fast and check
             * so we fill additional rows to cover nearest moves
             */
            val criteriaFactory = stateFactory!!.createDefaultFinishingCriteriaFactory()
            criteriaFactory.additionalRowCount = APPROXIMATE_ADDITIONAL_ROWS_COUNT
            val layouterFactory = stateFactory!!.createLayouterFactory(
                criteriaFactory,
                placerFactory.createRealPlacerFactory()
            )
            val backwardLayouter = layouterFactory.getBackwardLayouter(anchorView!!)
            val forwardLayouter = layouterFactory.getForwardLayouter(anchorView!!)
            fill(recycler, backwardLayouter, forwardLayouter)

            /* should be executed before {@link #layoutDisappearingViews} */
            if (scrollingController!!.normalizeGaps(recycler, null)) {
                Log.d(TAG, "normalize gaps")
                //we should re-layout with new anchor after normalizing gaps
                anchorView = anchorFactory!!.getAnchor()
                requestLayoutWithAnimations()
            }
            if (isAfterPreLayout) {
                //we should layout disappearing views after pre-layout to support natural movements)
                layoutDisappearingViews(recycler, backwardLayouter, forwardLayouter)
            }
            isAfterPreLayout = false
        }
        disappearingViewsManager!!.reset()
        if (!state.isMeasuring) {
            measureSupporter.onSizeChanged()
        }
    }

    override fun detachAndScrapAttachedViews(recycler: Recycler) {
        super.detachAndScrapAttachedViews(recycler)
        childViewPositions.clear()
    }

    /** layout disappearing view to support predictive animations  */
    private fun layoutDisappearingViews(
        recycler: Recycler,
        upLayouter: ILayouter,
        downLayouter: ILayouter
    ) {
        val upLayouter1: ILayouter
        val downLayouter1: ILayouter
        val criteriaFactory: ICriteriaFactory = InfiniteCriteriaFactory()
        val layouterFactory = stateFactory!!.createLayouterFactory(
            criteriaFactory,
            placerFactory.createDisappearingPlacerFactory()
        )
        val disappearingViews = disappearingViewsManager!!.getDisappearingViews(recycler)
        if (disappearingViews!!.size() > 0) {
            Log.d("disappearing views", "count = " + disappearingViews.size())
            Log.d("fill disappearing views", "")
            downLayouter1 = layouterFactory.buildForwardLayouter(downLayouter)

            //we should layout disappearing views left somewhere, just continue layout them in current layouter
            for (i in 0 until disappearingViews.forwardViews.size()) {
                val position = disappearingViews.forwardViews.keyAt(i)
                downLayouter1.placeView(recycler.getViewForPosition(position))
            }
            //layout last row
            downLayouter1.layoutRow()
            upLayouter1 = layouterFactory.buildBackwardLayouter(upLayouter)
            //we should layout disappearing views left somewhere, just continue layout them in current layouter
            for (i in 0 until disappearingViews.backwardViews.size()) {
                val position = disappearingViews.backwardViews.keyAt(i)
                upLayouter1.placeView(recycler.getViewForPosition(position))
            }

            //layout last row
            upLayouter1.layoutRow()
        }
    }

    /**
     * place all added views to cache (in case scrolling)...
     */
    private fun fillCache() {
        var i = 0
        while (i < childCount) {
            val view = getChildAt(i)
            val pos = getPosition(view!!)
            viewCache.put(pos, view)
            i++
        }
    }

    /**
     * place all views on theirs right places according to current state
     */
    private fun fill(recycler: Recycler, backwardLayouter: ILayouter, forwardLayouter: ILayouter) {
        val startingPos = anchorView!!.position?:0
        fillCache()

        //... and remove from layout
        for (i in 0 until viewCache.size()) {
            detachView(viewCache.valueAt(i))
        }
        logger!!.onStartLayouter(startingPos - 1)

        /* there is no sense to perform backward layouting when anchor is null.
           null anchor means that layout will be performed from absolutely top corner with start at anchor position
        */
        if (anchorView!!.anchorViewRect != null) {
            //up layouter should be invoked earlier than down layouter, because views with lower positions positioned above anchorView
            //start from anchor position
            fillWithLayouter(recycler, backwardLayouter, startingPos - 1)
        }
        logger!!.onStartLayouter(startingPos)

        //start from anchor position
        fillWithLayouter(recycler, forwardLayouter, startingPos)
        logger!!.onAfterLayouter()
        //move to trash everything, which haven't used in this layout cycle
        //that views gone from a screen or was removed outside from adapter
        for (i in 0 until viewCache.size()) {
            removeAndRecycleView(viewCache.valueAt(i), recycler)
            logger!!.onRemovedAndRecycled(i)
        }
        canvas!!.findBorderViews()
        buildChildWithPositionsMap()
        viewCache.clear()
        logger!!.onAfterRemovingViews()
    }


    private fun buildChildWithPositionsMap() {
        childViewPositions.clear()
        for (view in childViews) {
            val position = getPosition(view)
            childViewPositions.put(position, view)
        }
    }

    /**
     * place views in layout started from chosen position with chosen layouter
     */
    private fun fillWithLayouter(recycler: Recycler, layouter: ILayouter, startingPos: Int) {
        if (startingPos < 0) return
        val iterator = layouter.positionIterator()
        iterator.move(startingPos)
        while (iterator.hasNext()) {
            val pos = iterator.next()
            var view = viewCache[pos]
            if (view == null) { // we don't have view from previous layouter stage, request new one
                view = try {
                    recycler.getViewForPosition(pos)
                } catch (e: IndexOutOfBoundsException) {
                    /* WTF sometimes on prediction animation playing in case very fast sequential changes in adapter
                             * {@link #getItemCount} could return value bigger than real count of items
                             * & {@link RecyclerView.Recycler#getViewForPosition(int)} throws exception in this case!
                             * to handle it, just leave the loop*/
                    break
                }
                logger?.onItemRequested()

                if (!layouter.placeView(view)) {
                    /* reached end of visible bounds, exit.
                   recycle view, which was requested previously
                    */
                    recycler.recycleView(view)
                    logger?.onItemRecycled()

                    break
                }
            } else { //we have detached views from previous layouter stage, attach it if needed
                if (!layouter.onAttachView(view)) {
                    break
                }

                //remove reattached view from cache
                viewCache.remove(pos)
            }
        }
        logger!!.onFinishedLayouter()

        //layout last row, in case iterator fully processed
        layouter.layoutRow()
    }

    /**
     * recycler should contain all recycled views from a longest row, not just 2 holders by default
     */
    private fun calcRecyclerCacheSize(recycler: Recycler) {
        val viewsInRow =
            if (maxViewsInRow == 0) INT_ROW_SIZE_APPROXIMATELY_FOR_CACHE else maxViewsInRow
        recycler.setViewCacheSize((viewsInRow * FAST_SCROLLING_COEFFICIENT).toInt())
    }

    /**
     * after several layout changes our item views probably haven't placed on right places,
     * because we don't memorize whole positions of items.
     * So them should be normalized to real positions when we can do it.
     */
    private fun performNormalizationIfNeeded() {
        if (cacheNormalizationPosition != null && childCount > 0) {
            val firstView = getChildAt(0)
            val firstViewPosition = getPosition(firstView!!)
            if (firstViewPosition < cacheNormalizationPosition!! ||
                cacheNormalizationPosition == 0 && cacheNormalizationPosition == firstViewPosition
            ) {
                //perform normalization when we have reached previous position then normalization position
                Log.d("normalization", "position = $cacheNormalizationPosition top view position = $firstViewPosition")
                Log.d(TAG, "cache purged from position $firstViewPosition")
                viewPositionsStorage.purgeCacheFromPosition(firstViewPosition)
                //reset normalization position
                cacheNormalizationPosition = null
                requestLayoutWithAnimations()
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // measure
    ///////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    override fun setMeasuredDimension(widthSize: Int, heightSize: Int) {
        measureSupporter.measure(widthSize, heightSize)
        Log.i(TAG, "measured dimension = $heightSize")
        super.setMeasuredDimension(
            measureSupporter.getMeasuredWidth(),
            measureSupporter.getMeasuredHeight()
        )
    }


    ///////////////////////////////////////////////////////////////////////////
    // data set changed events
    ///////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?
    ) {
        if (oldAdapter != null && measureSupporter.isRegistered()) {
            try {
                measureSupporter.setRegistered(false)
                oldAdapter.unregisterAdapterDataObserver((measureSupporter as AdapterDataObserver))
            } catch (e: IllegalStateException) {
                //skip unregister errors
            }
        }
        if (newAdapter != null) {
            measureSupporter.setRegistered(true)
            newAdapter.registerAdapterDataObserver((measureSupporter as AdapterDataObserver))
        }
        //Completely scrap the existing layout
        removeAllViews()
    }

    /**
     * {@inheritDoc}
     */
    override fun onItemsRemoved(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        Log.d(
            "onItemsRemoved",
            "starts from = $positionStart, item count = $itemCount",
            LogSwitcherFactory.ADAPTER_ACTIONS
        )
        super.onItemsRemoved(recyclerView, positionStart, itemCount)
        onLayoutUpdatedFromPosition(positionStart)
        measureSupporter.onItemsRemoved(recyclerView)
    }

    /**
     * {@inheritDoc}
     */
    override fun onItemsAdded(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        Log.d(
            "onItemsAdded",
            "starts from = $positionStart, item count = $itemCount",
            LogSwitcherFactory.ADAPTER_ACTIONS
        )
        super.onItemsAdded(recyclerView, positionStart, itemCount)
        onLayoutUpdatedFromPosition(positionStart)
    }

    /**
     * {@inheritDoc}
     */
    override fun onItemsChanged(recyclerView: RecyclerView) {
        Log.d("onItemsChanged", "", LogSwitcherFactory.ADAPTER_ACTIONS)
        super.onItemsChanged(recyclerView)
        viewPositionsStorage.purge()
        onLayoutUpdatedFromPosition(0)
    }

    /**
     * {@inheritDoc}
     */
    override fun onItemsUpdated(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        Log.d(
            "onItemsUpdated",
            "starts from = $positionStart, item count = $itemCount",
            LogSwitcherFactory.ADAPTER_ACTIONS
        )
        super.onItemsUpdated(recyclerView, positionStart, itemCount)
        onLayoutUpdatedFromPosition(positionStart)
    }

    /**
     * {@inheritDoc}
     */
    override fun onItemsUpdated(
        recyclerView: RecyclerView,
        positionStart: Int,
        itemCount: Int,
        payload: Any?
    ) {
        onItemsUpdated(recyclerView, positionStart, itemCount)
    }

    /**
     * {@inheritDoc}
     */
    override fun onItemsMoved(recyclerView: RecyclerView, from: Int, to: Int, itemCount: Int) {
        Log.d(
            "onItemsMoved",
            String.format(Locale.US, "from = %d, to = %d, itemCount = %d", from, to, itemCount),
            LogSwitcherFactory.ADAPTER_ACTIONS
        )
        super.onItemsMoved(recyclerView, from, to, itemCount)
        onLayoutUpdatedFromPosition(from.coerceAtMost(to))
    }

    /** update cache according to data changes  */
    private fun onLayoutUpdatedFromPosition(position: Int) {
        Log.d(TAG, "cache purged from position $position")
        viewPositionsStorage.purgeCacheFromPosition(position)
        val startRowPos = viewPositionsStorage.getStartOfRow(position)
        cacheNormalizationPosition =
            if (cacheNormalizationPosition == null) startRowPos else (cacheNormalizationPosition!!).coerceAtMost(
                startRowPos
            )
    }


    ///////////////////////////////////////////////////////////////////////////
    // Scrolling
    ///////////////////////////////////////////////////////////////////////////

    /**
     * When smooth scrollbar is enabled, the position and size of the scrollbar thumb is computed
     * based on the number of visible pixels in the visible items. This however assumes that all
     * list items have similar or equal widths or heights (depending on list orientation).
     *
     * Also this is [ChipLayoutManager] specific issue, that we can't predict exact count of items on screen
     * in general case, because we can't predict items count in row.
     * So to enable it you should accomplish one of those conditions:
     *
     *  *  Your items have same width and height
     *  *  You have [ChipLayoutManager.setMaxViewsInRow] set and you able to make sure, that there won't be many rows with lower items count.
     * The best is none.
     *
     *
     * If you use a list in which items have different dimensions, the scrollbar will change
     * appearance as the user scrolls through the list. To avoid this issue,  you need to disable
     * this property.
     *
     * When smooth scrollbar is disabled, the position and size of the scrollbar thumb is based
     * solely on the number of items in the adapter and the position of the visible items inside
     * the adapter. This provides a stable scrollbar as the user navigates through a list of items
     * with varying widths / heights.
     *
     * @param enabled Whether or not to enable smooth scrollbar.
     *
     * @see .isSmoothScrollbarEnabled
     */
    override fun setSmoothScrollbarEnabled(enabled: Boolean) {
        isSmoothScrollbarEnabled = enabled
    }

    /**
     * Returns the current state of the smooth scrollbar feature. It is NOT enabled by default.
     *
     * @return True if smooth scrollbar is enabled, false otherwise.
     *
     * @see .setSmoothScrollbarEnabled
     */
    override fun isSmoothScrollbarEnabled(): Boolean {
        return isSmoothScrollbarEnabled
    }


    /**
     * {@inheritDoc}
     */
    override fun scrollToPosition(position: Int) {
        if (position >= itemCount || position < 0) {
            Log.e("span layout manager", "Cannot scroll to $position, item count $itemCount")
            return
        }
        var position1 = position
        val lastCachePosition = viewPositionsStorage.getLastCachePosition()
        cacheNormalizationPosition =
            if (cacheNormalizationPosition != null) cacheNormalizationPosition else lastCachePosition
        if (lastCachePosition != null && position < lastCachePosition) {
            position1 = viewPositionsStorage.getStartOfRow(position)
        }
        anchorView = anchorFactory!!.createNotFound()
        anchorView!!.position = position1

        //Trigger a new view layout
        super.requestLayout()
    }

    /**
     * {@inheritDoc}
     */
    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State?,
        position: Int
    ) {
        if (position >= itemCount || position < 0) {
            Log.e("span layout manager", "Cannot scroll to $position, item count $itemCount")
            return
        }
        val scroller = scrollingController!!.createSmoothScroller(
            recyclerView.context, position, 150,
            anchorView!!
        )
        scroller.targetPosition = position
        startSmoothScroll(scroller)
    }

    override fun canScrollHorizontally(): Boolean {
        return scrollingController!!.canScrollHorizontally()
    }

    override fun canScrollVertically(): Boolean {
        return scrollingController!!.canScrollVertically()
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun scrollVerticallyBy(dy: Int, recycler: Recycler?, state: RecyclerView.State?): Int {
        return scrollingController!!.scrollVerticallyBy(dy, recycler, state!!)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: Recycler?,
        state: RecyclerView.State?
    ): Int {
        return scrollingController!!.scrollHorizontallyBy(dx, recycler, state!!)
    }

    fun verticalScrollingController(): VerticalScrollingController {
        return VerticalScrollingController(this, stateFactory!!, this)
    }

    fun horizontalScrollingController(): HorizontalScrollingController {
        return HorizontalScrollingController(this, stateFactory!!, this)
    }

    override fun onScrolled(scrollingController: IScrollingController, recycler: Recycler?, state: RecyclerView.State) {
        performNormalizationIfNeeded()
        anchorView = anchorFactory!!.getAnchor()
        val criteriaFactory = stateFactory!!.createDefaultFinishingCriteriaFactory()
        criteriaFactory.additionalRowCount = 1
        val factory = stateFactory!!.createLayouterFactory(criteriaFactory, placerFactory.createRealPlacerFactory())
        fill(recycler!!, factory.getBackwardLayouter(anchorView!!), factory.getForwardLayouter(anchorView!!))
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun computeVerticalScrollOffset(state: RecyclerView.State): Int {
        return scrollingController!!.computeVerticalScrollOffset(state)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun computeVerticalScrollExtent(state: RecyclerView.State): Int {
        return scrollingController!!.computeVerticalScrollExtent(state)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun computeVerticalScrollRange(state: RecyclerView.State): Int {
        return scrollingController!!.computeVerticalScrollRange(state)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun computeHorizontalScrollExtent(state: RecyclerView.State): Int {
        return scrollingController!!.computeHorizontalScrollExtent(state)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun computeHorizontalScrollOffset(state: RecyclerView.State): Int {
        return scrollingController!!.computeHorizontalScrollOffset(state)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun computeHorizontalScrollRange(state: RecyclerView.State): Int {
        return scrollingController!!.computeHorizontalScrollRange(state)
    }
}