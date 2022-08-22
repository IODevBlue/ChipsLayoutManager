package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import android.view.View
import androidx.annotation.CallSuper
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IBorder
import com.blueiobase.api.android.chiplayoutmanager.annotation.SpanLayoutChildGravity
import com.blueiobase.api.android.chiplayoutmanager.cache.contract.IViewCacheStorage
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IChildGravityResolver
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifiersFactory
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouterListener
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacer
import com.blueiobase.api.android.chiplayoutmanager.util.AssertionUtils.assertNotNull
import java.util.*


abstract class AbstractLayouter(builder: Builder): ILayouter, IBorder {

    companion object {

        abstract class Builder {

            var offsetRect: Rect? = null
            var layoutManager: ChipsLayoutManager? = null
            var cacheStorage: IViewCacheStorage? = null
            var rowStrategy: IRowStrategy? = null
            var border: IBorder? = null
            var gravityModifiersFactory: IGravityModifiersFactory? = null
            var childGravityResolver: IChildGravityResolver? = null
            var finishingCriteria: IFinishingCriteria? = null
            var placer: IPlacer? = null
            var breaker: ILayoutRowBreaker? = null
            var layouterListeners = HashSet<ILayouterListener>()
            var positionIterator: AbstractPositionIterator? = null

            fun offsetRect(offsetRect: Rect): Builder {
                this.offsetRect = offsetRect
                return this
            }

            fun layoutManager(layoutManager: ChipsLayoutManager): Builder {
                this.layoutManager = layoutManager
                return this
            }

            fun cacheStorage(cacheStorage: IViewCacheStorage): Builder {
                this.cacheStorage = cacheStorage
                return this
            }

            fun rowStrategy(rowStrategy: IRowStrategy?): Builder {
                this.rowStrategy = rowStrategy
                return this
            }

            fun canvas(border: IBorder?): Builder {
                this.border = border
                return this
            }

            fun gravityModifiersFactory(gravityModifiersFactory: IGravityModifiersFactory): Builder {
                this.gravityModifiersFactory = gravityModifiersFactory
                return this
            }

            fun childGravityResolver(childGravityResolver: IChildGravityResolver?): Builder {
                this.childGravityResolver = childGravityResolver
                return this
            }

            fun finishingCriteria(finishingCriteria: IFinishingCriteria): Builder {
                this.finishingCriteria = finishingCriteria
                return this
            }

            fun placer(placer: IPlacer): Builder {
                this.placer = placer
                return this
            }

            fun addLayouterListener(layouterListener: ILayouterListener?): Builder { //TODO: Make non null?
                layouterListener?.let { this.layouterListeners.add(it) }
                return this
            }

            fun breaker(breaker: ILayoutRowBreaker): Builder {
                assertNotNull(breaker, "breaker shouldn't be null")
                this.breaker = breaker
                return this
            }

            fun addLayouterListeners(layouterListeners: List<ILayouterListener>): Builder {
                this.layouterListeners.addAll(layouterListeners)
                return this
            }

            fun positionIterator(positionIterator: AbstractPositionIterator?): Builder {
                this.positionIterator = positionIterator
                return this
            }

            protected abstract fun createLayouter(): AbstractLayouter

            fun build(): AbstractLayouter {
                checkNotNull(layoutManager) { "layoutManager cannot be null, call #layoutManager()" }
                checkNotNull(breaker) { "breaker cannot be null, call #breaker()" }
                checkNotNull(border) { "border cannot be null, call #border()" }
                checkNotNull(cacheStorage) { "cacheStorage cannot be null, call #cacheStorage()" }
                checkNotNull(rowStrategy) { "rowStrategy cannot be null, call #rowStrategy()" }
                checkNotNull(offsetRect) { "offsetRect cannot be null, call #offsetRect()" }
                checkNotNull(finishingCriteria) { "finishingCriteria cannot be null, call #finishingCriteria()" }
                checkNotNull(placer) { "placer cannot be null, call #placer()" }
                checkNotNull(gravityModifiersFactory) { "gravityModifiersFactory cannot be null, call #gravityModifiersFactory()" }
                checkNotNull(childGravityResolver) { "childGravityResolver cannot be null, call #childGravityResolver()" }
                checkNotNull(positionIterator) { "positionIterator cannot be null, call #positionIterator()" }
                return createLayouter()
            }
        }
    }

    var currentViewWidth = 0
        private set

    var currentViewHeight = 0
        private set

    var currentViewPosition = 0
        private set

    var rowViews = ArrayList<Pair<Rect, View>>()

    /** bottom of current row */
    private var viewBottom = 0

    /** top of current row */
    private var viewTop = 0

    /** right offset  */
    var viewRight = 0

    /** left offset */
    var viewLeft = 0

    private var rowSize = 0
    private var previousRowSize = 0

    /** is row completed when [.layoutRow] called */
    var isRowCompleted = false
        private set

    ///////////////////////////////////////////////////////////////////////////
    // input dependencies
    ///////////////////////////////////////////////////////////////////////////
    var layoutManager: ChipsLayoutManager
        private set
    var cacheStorage: IViewCacheStorage
        private set
    private var border: IBorder
    private var childGravityResolver: IChildGravityResolver
    var finishingCriteria: IFinishingCriteria
    var placer: IPlacer
    private var breaker: ILayoutRowBreaker
    private var rowStrategy: IRowStrategy
    private var layouterListeners = HashSet<ILayouterListener>()
    private var gravityModifiersFactory: IGravityModifiersFactory
    private var positionIterator: AbstractPositionIterator

    init {
        builder.let {
            //--- read builder
            layoutManager = it.layoutManager!!
            cacheStorage = it.cacheStorage!!
            border = it.border!!
            childGravityResolver = it.childGravityResolver!!
            finishingCriteria = it.finishingCriteria!!
            placer = it.placer!!
            it.offsetRect!!.apply {
                viewTop = top
                viewBottom = bottom
                viewRight = right
                viewLeft = left

            }
            layouterListeners = it.layouterListeners
            breaker = it.breaker!!
            gravityModifiersFactory = it.gravityModifiersFactory!!
            rowStrategy = it.rowStrategy!!
            positionIterator = it.positionIterator!!
            //--- end read builder
        }
    }

    override fun positionIterator(): AbstractPositionIterator {
        return positionIterator
    }


    override fun getCurrentRowItems(): ArrayList<Item> {
        val items = ArrayList<Item>()
        val mutableRowViews = LinkedList(rowViews)
        if (isReverseOrder()) {
            mutableRowViews.reverse()
        }
        for (rowView in mutableRowViews) {
            items.add(
                Item(
                    rowView!!.first, layoutManager.getPosition(rowView.second)
                )
            )
        }
        return items
    }

    override fun addLayouterListener(layouterListener: ILayouterListener?) {
        layouterListener?.let{
            layouterListeners.add(layouterListener)
        }
    }

    override fun removeLayouterListener(layouterListener: ILayouterListener?) {
        layouterListeners.remove(layouterListener)
    }

    private fun notifyLayouterListeners() {
        for (layouterListener in layouterListeners) {
            layouterListener.onLayoutRow(this)
        }
    }

    override fun getPreviousRowSize() = previousRowSize

    /** read view params to memory  */
    private fun calculateView(view: View) {
        layoutManager.apply {
            currentViewHeight = getDecoratedMeasuredHeight(view)
            currentViewWidth = getDecoratedMeasuredWidth(view)
            currentViewPosition = getPosition(view)

        }
    }

    /** calculate view positions, view won't be actually added to layout when calling this method
     * @return true if view successfully placed, false if view can't be placed because out of space on screen and have to be recycled
     */
    @CallSuper
    override fun placeView(view: View): Boolean {
        layoutManager.measureChildWithMargins(view, 0, 0)
        calculateView(view)
        if (canNotBePlacedInCurrentRow()) {
            isRowCompleted = true
            layoutRow()
        }
        if (isFinishedLayouting()) return false
        rowSize++
        val rect = createViewRect(view)
        rowViews.add(Pair(rect, view))
        return true
    }

    /** if all necessary view have placed */
    fun isFinishedLayouting() = finishingCriteria.isFinishedLayouting(this)

    /** check if we can not add current view to row
     * we determine it on the next layouter step, because we need next view size to determine whether it fits in row or not  */
    fun canNotBePlacedInCurrentRow() = breaker.isRowBroke(this)

    /** factory method for Rect, where view will be placed. Creation based on inner layouter parameters  */
    abstract fun createViewRect(view: View): Rect

    /** check whether items in [.rowViews] are in reverse order. It is true for backward layouters  */
    abstract fun isReverseOrder(): Boolean

    /** called when layouter ready to add row to border. Children could perform normalization actions on created row */
    abstract fun onPreLayout()

    /** called after row have been layouted. Children should prepare new row here.  */
    abstract fun onAfterLayout()

    abstract fun isAttachedViewFromNewRow(view: View): Boolean

    abstract fun onInterceptAttachView(view: View)

    /** Read layouter state from current attached view. We need only last of it, but we can't determine here which is last.
     * Based on characteristics of last attached view, layouter algorithm will be able to continue placing from it.
     * This method have to be called on attaching view */
    @CallSuper
    override fun onAttachView(view: View): Boolean {
        calculateView(view)
        if (isAttachedViewFromNewRow(view)) {
            //new row, reset row size
            notifyLayouterListeners()
            rowSize = 0
        }
        onInterceptAttachView(view)
        if (isFinishedLayouting()) return false
        rowSize++
        layoutManager.attachView(view)
        return true
    }

    /** add views from current row to layout */
    override fun layoutRow() {
        onPreLayout()

        //apply modifiers to whole row
        if (rowViews.size > 0) {
            rowStrategy.applyStrategy(this, getCurrentRowItems())
        }
        /** layout pre-calculated row on a recyclerView border  */
        for (rowViewRectPair in rowViews) {
            var viewRect = rowViewRectPair.first
            val view = rowViewRectPair.second
            viewRect = applyChildGravity(view, viewRect)
            //add view to layout
            placer.addView(view)

            //layout whole views in a row
            layoutManager.layoutDecorated(
                view,
                viewRect.left,
                viewRect.top,
                viewRect.right,
                viewRect.bottom
            )
        }
        onAfterLayout()
        notifyLayouterListeners()
        previousRowSize = rowSize
        //clear row data
        rowSize = 0
        rowViews.clear()
        isRowCompleted = false
    }

    /** by default items placed and attached to a top of the row.
     * Modify theirs relative positions according to the selected child gravity
     * @return modified rect with applied gravity
     */
    private fun applyChildGravity(view: View, viewRect: Rect): Rect {
        @SpanLayoutChildGravity val viewGravity =
            childGravityResolver.getItemGravity(layoutManager.getPosition(view))
        val gravityModifier = gravityModifiersFactory.getGravityModifier(viewGravity)
        return gravityModifier.modifyChildRect(getStartRowBorder(), getEndRowBorder(), viewRect)
    }

    /** get count of items inside current row  */
    override fun getRowSize() = rowSize

    override fun getViewTop() = viewTop

    fun setViewTop(viewTop: Int) {
        this.viewTop = viewTop
    }

    /** get a start coordinate of row border which is perpendicular to row general extension */
    abstract fun getStartRowBorder(): Int

    /** get an end coordinate of row border which is perpendicular to row general extension */
    abstract fun getEndRowBorder(): Int

    override fun getRowRect() = Rect(getCanvasLeftBorder(), getViewTop(), getCanvasRightBorder(), getViewBottom())

    override fun getViewBottom() = viewBottom

    fun setViewBottom(viewBottom: Int) {
        this.viewBottom = viewBottom
    }

    fun getOffsetRect() = Rect(viewLeft, viewTop, viewRight, viewBottom)

    abstract fun getRowLength(): Int

    ///////////////////////////////////////////////////////////////////////////
    // border delegate
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // border delegate
    ///////////////////////////////////////////////////////////////////////////
    override fun getCanvasRightBorder() = border.getCanvasRightBorder()

    override fun getCanvasBottomBorder() = border.getCanvasBottomBorder()

    override fun getCanvasLeftBorder() = border.getCanvasLeftBorder()

    override fun getCanvasTopBorder() = border.getCanvasTopBorder()

}