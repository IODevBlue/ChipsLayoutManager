package com.blueiobase.api.android.chiplayoutmanager.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * This class is responsible for traversing each child [View] object that the provided
 * [layout manager][RecyclerView.LayoutManager] handles in the layout.
 *
 * @author IO DevBlue
 * @since 1.0.0
 */
class ChildViewsIterable(private val layoutManager: RecyclerView.LayoutManager): Iterable<View> {

    override fun iterator(): Iterator<View> {
        return object : Iterator<View> {
            var i = 0
            override fun hasNext(): Boolean {
                return i < layoutManager.childCount
            }

            override fun next(): View {
                return layoutManager.getChildAt(i++)!!
            }
        }
    }

    /**
     * Returns the number of [View] objects currently managed by the [layout manager][RecyclerView.LayoutManager].
     */
    fun size() = layoutManager.childCount
}