package com.blueiobase.api.android.chiplayoutmanager.main.contract

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState


interface IScrollingController {

    fun createSmoothScroller(context: Context, position: Int, timeMs: Int, anchor: AnchorViewState): SmoothScroller

    fun canScrollVertically(): Boolean

    fun canScrollHorizontally(): Boolean

    /**
     * calculate offset of views while scrolling, layout items on new places
     */
    fun scrollVerticallyBy(d: Int, recycler: Recycler?, state: RecyclerView.State): Int

    fun scrollHorizontallyBy(d: Int, recycler: Recycler?, state: RecyclerView.State): Int

    /** changes may cause gaps on the UI, try to fix them  */
    fun normalizeGaps(recycler: Recycler?, state: RecyclerView.State?): Boolean

    /** @see ChipsLayoutManager.computeVerticalScrollOffset
     */
    fun computeVerticalScrollOffset(state: RecyclerView.State): Int

    /** @see ChipsLayoutManager.computeVerticalScrollExtent
     */
    fun computeVerticalScrollExtent(state: RecyclerView.State): Int

    /** @see ChipsLayoutManager.computeVerticalScrollRange
     */
    fun computeVerticalScrollRange(state: RecyclerView.State): Int

    /** @see ChipsLayoutManager.computeHorizontalScrollOffset
     */
    fun computeHorizontalScrollOffset(state: RecyclerView.State): Int

    /** @see ChipsLayoutManager.computeHorizontalScrollExtent
     */
    fun computeHorizontalScrollExtent(state: RecyclerView.State): Int

    /** @see ChipsLayoutManager.computeHorizontalScrollRange
     */
    fun computeHorizontalScrollRange(state: RecyclerView.State): Int

}