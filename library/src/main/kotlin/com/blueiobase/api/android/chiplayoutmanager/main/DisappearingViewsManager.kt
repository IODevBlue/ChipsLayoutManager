package com.blueiobase.api.android.chiplayoutmanager.main

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IDisappearingViewsManager
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ICanvas
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IStateFactory

class DisappearingViewsManager(
    private val canvas: ICanvas,
    private val childViews: ChildViewsIterable,
    private val stateFactory: IStateFactory
): IDisappearingViewsManager {


    /* in pre-layouter drawing we need item count with items will be actually deleted to pre-draw appearing items properly
    * buf value */
    private var deletingItemsOnScreenCount = 0

    inner class DisappearingViewsContainer {
        val backwardViews = SparseArray<View>()
        val forwardViews = SparseArray<View>()

        fun size() = backwardViews.size() + forwardViews.size()

    }

    /** @return views which moved from screen, but not deleted
     */
    override fun getDisappearingViews(recycler: Recycler): DisappearingViewsContainer {
        val scrapList = recycler.scrapList
        val container = DisappearingViewsContainer()
        for (holder in scrapList) {
            val child: View = holder.itemView
            val lp = child.layoutParams as RecyclerView.LayoutParams
            if (!lp.isItemRemoved) {
                if (lp.viewAdapterPosition < canvas.getMinPositionOnScreen()!!) {
                    container.backwardViews.put(lp.viewAdapterPosition, child)
                } else if (lp.viewAdapterPosition > canvas.getMaxPositionOnScreen()!!) {
                    container.forwardViews.put(lp.viewAdapterPosition, child)
                }
            }
        }
        return container
    }

    /** during pre-layout calculate approximate height which will be free after moving items offscreen (removed or moved)
     * @return approximate height of disappearing views. Could be bigger, than accurate value.
     */
    override fun calcDisappearingViewsLength(recycler: Recycler): Int {
        var removedLength = 0
        var minStart = Int.MAX_VALUE
        var maxEnd = Int.MIN_VALUE
        for (view in childViews) {
            val lp = view.layoutParams as RecyclerView.LayoutParams
            var probablyMovedFromScreen = false
            if (!lp.isItemRemoved) {
                //view won't be removed, but maybe it is moved offscreen
                var pos = lp.viewLayoutPosition
                pos = recycler.convertPreLayoutPositionToPostLayout(pos)
                probablyMovedFromScreen =
                    pos < canvas.getMinPositionOnScreen()!! || pos > canvas.getMaxPositionOnScreen()!!
            }
            if (lp.isItemRemoved || probablyMovedFromScreen) {
                deletingItemsOnScreenCount++
                minStart = minStart.coerceAtMost(stateFactory.getStart(view))
                maxEnd = maxEnd.coerceAtLeast(stateFactory.getEnd(view))
            }
        }
        if (minStart != Int.MAX_VALUE) {
            removedLength = maxEnd - minStart
        }
        return removedLength
    }

    override fun getDeletingItemsOnScreenCount(): Int {
        return deletingItemsOnScreenCount
    }

    override fun reset() {
        deletingItemsOnScreenCount = 0
    }
}
