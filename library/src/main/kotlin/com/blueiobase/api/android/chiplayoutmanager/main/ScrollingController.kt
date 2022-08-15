package com.blueiobase.api.android.chiplayoutmanager.main

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IScrollingController
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IStateFactory
import kotlin.math.abs
import kotlin.math.roundToInt


abstract class ScrollingController(
    private val layoutManager: ChipLayoutManager,
    private val stateFactory: IStateFactory,
    private val scrollerListener: IScrollerListener
): IScrollingController {

    var canvas = layoutManager.canvas

    interface IScrollerListener {
        fun onScrolled(scrollingController: IScrollingController, recycler: Recycler?, state: RecyclerView.State)
    }

    fun calculateEndGap(): Int {
        if (layoutManager.childCount == 0) return 0
        val visibleViewsCount: Int = layoutManager.getCompletelyVisibleViewsCount()
        if (visibleViewsCount == layoutManager.itemCount) return 0
        var diff: Int
        stateFactory.apply { 
            val currentEnd = getEndViewBound()
            val desiredEnd = getEndAfterPadding()
            diff = desiredEnd - currentEnd
        }
        return if (diff < 0) 0 else diff
    }

    fun calculateStartGap(): Int {
        if (layoutManager.childCount == 0) return 0
        var diff: Int
        stateFactory.apply {
            val currentStart = getStartViewBound()
            val desiredStart = getStartAfterPadding()
            diff = currentStart - desiredStart
        }
        return if (diff < 0) 0 else diff
    }

    override fun normalizeGaps(recycler: Recycler?, state: RecyclerView.State?): Boolean {
        val backwardGap = calculateStartGap()
        if (backwardGap > 0) {
            offsetChildren(-backwardGap)
            //if we have normalized start gap, normalizing bottom have no sense
            return true
        }
        val forwardGap = calculateEndGap()
        if (forwardGap > 0) {
            state?.let {
                scrollBy(-forwardGap, recycler, it)
            }
            return true
        }
        return false
    }

    fun calcOffset(d: Int): Int {
        val childCount = layoutManager.childCount
        if (childCount == 0) return 0
        var delta = 0
        if (d < 0) {   //if content scrolled down
            delta = onContentScrolledBackward(d)
        } else if (d > 0) { //if content scrolled up
            delta = onContentScrolledForward(d)
        }
        return delta
    }

    /**
     * invoked when content scrolled forward (return to older items)
     *
     * @param d not processed changing of x or y axis, depending on layoutManager state
     * @return delta. Calculated changing of x or y axis, depending on layoutManager state
     */
    fun onContentScrolledBackward(d: Int): Int {
        val delta: Int
        val anchor = layoutManager.anchorView
        if (anchor?.anchorViewRect == null) {
            return 0
        }
        delta = if (anchor.position != 0) { //in case 0 position haven't added in layout yet
            d
        } else { //in case top view is a first view in adapter and wouldn't be any other view above
            val startBorder = stateFactory.getStartAfterPadding()
            val viewStart = stateFactory.getStart(anchor)
            val distance: Int = viewStart - startBorder
            if (distance >= 0) {
                // in case over scroll on top border
                distance
            } else {
                //in case first child showed partially
                distance.coerceAtLeast(d)
            }
        }
        return delta
    }

    /**
     * invoked when content scrolled up (to newer items)
     *
     * @param d not processed changing of x or y axis, depending on layoutManager state
     * @return delta. Calculated changing of x or y axis, depending on layoutManager state
     */
    fun onContentScrolledForward(d: Int): Int {
        val childCount = layoutManager.childCount
        val itemCount = layoutManager.itemCount
        val delta: Int
        val lastView = layoutManager.getChildAt(childCount - 1)
        val lastViewAdapterPos = layoutManager.getPosition(lastView!!) //TODO: Possible nullity
        delta = if (lastViewAdapterPos < itemCount - 1) { //in case lower view isn't the last view in adapter
            d
        } else { //in case lower view is the last view in adapter and wouldn't be any other view below
            val viewEnd = stateFactory.getEndViewBound()
            val parentEnd = stateFactory.getEndAfterPadding()
            (viewEnd - parentEnd).coerceAtMost(d)
        }
        return delta
    }

    abstract fun offsetChildren(d: Int)
    override fun scrollHorizontallyBy(d: Int, recycler: Recycler?, state: RecyclerView.State): Int {
        return if (canScrollHorizontally()) scrollBy(d, recycler, state) else 0
    }

    override fun scrollVerticallyBy(d: Int, recycler: Recycler?, state: RecyclerView.State): Int {
        return if (canScrollVertically()) scrollBy(d, recycler, state) else 0
    }

    private fun scrollBy(d: Int, recycler: Recycler?, state: RecyclerView.State): Int {
        val d1 = calcOffset(d)
        offsetChildren(-d)
        scrollerListener.onScrolled(this, recycler, state)
        return d1
    }

    private fun getLaidOutArea() = stateFactory.getEndViewBound() - stateFactory.getStartViewBound()

    /** @see ChipLayoutManager.computeVerticalScrollOffset
     * @see ChipLayoutManager.computeHorizontalScrollOffset
     */
    private fun computeScrollOffset(state: RecyclerView.State): Int {
        layoutManager.apply {
            if (childCount == 0 || state.itemCount == 0) return 0

            val firstVisiblePos = findFirstVisibleItemPosition()
            val lastVisiblePos = findLastVisibleItemPosition()
            val itemsBefore = 0.coerceAtLeast(firstVisiblePos)
            if (!isSmoothScrollbarEnabled()) return itemsBefore

            val itemRange = abs(firstVisiblePos - lastVisiblePos) + 1
            val avgSizePerRow = getLaidOutArea().toFloat() / itemRange
            return (itemsBefore * avgSizePerRow + (stateFactory.getStartAfterPadding() - stateFactory.getStartViewBound())).roundToInt()
        }

    }

    /** @see ChipLayoutManager.computeVerticalScrollExtent
     * @see ChipLayoutManager.computeHorizontalScrollExtent
     */
    private fun computeScrollExtent(state: RecyclerView.State): Int {
        if (layoutManager.childCount == 0 || state.itemCount == 0) {
            return 0
        }
        val firstVisiblePos = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePos = layoutManager.findLastVisibleItemPosition()
        return if (!layoutManager.isSmoothScrollbarEnabled()) {
            abs(lastVisiblePos - firstVisiblePos) + 1
        } else stateFactory.getTotalSpace().coerceAtMost(getLaidOutArea())
    }

    private fun computeScrollRange(state: RecyclerView.State): Int {
        if (layoutManager.childCount == 0 || state.itemCount == 0) {
            return 0
        }
        if (!layoutManager.isSmoothScrollbarEnabled()) {
            return state.itemCount
        }
        val firstVisiblePos = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePos = layoutManager.findLastVisibleItemPosition()

        // smooth scrollbar enabled. try to estimate better.
        val laidOutRange = abs(firstVisiblePos - lastVisiblePos) + 1

        // estimate a size for full list.
        return (getLaidOutArea().toFloat() / laidOutRange * state.itemCount).toInt()
    }

    override fun computeVerticalScrollExtent(state: RecyclerView.State): Int {
        return if (canScrollVertically()) computeScrollExtent(state) else 0
    }

    override fun computeVerticalScrollRange(state: RecyclerView.State): Int {
        return if (canScrollVertically()) computeScrollRange(state) else 0
    }

    override fun computeVerticalScrollOffset(state: RecyclerView.State): Int {
        return if (canScrollVertically()) computeScrollOffset(state) else 0
    }

    override fun computeHorizontalScrollRange(state: RecyclerView.State): Int {
        return if (canScrollHorizontally()) computeScrollRange(state) else 0
    }

    override fun computeHorizontalScrollOffset(state: RecyclerView.State): Int {
        return if (canScrollHorizontally()) computeScrollOffset(state) else 0
    }

    override fun computeHorizontalScrollExtent(state: RecyclerView.State): Int {
        return if (canScrollHorizontally()) computeScrollExtent(state) else 0
    }


}