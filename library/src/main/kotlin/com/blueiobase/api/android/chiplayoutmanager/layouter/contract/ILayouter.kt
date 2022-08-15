package com.blueiobase.api.android.chiplayoutmanager.layouter.contract

import android.graphics.Rect
import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractPositionIterator
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item

interface ILayouter {

    /** add views from current row to layout */
    fun layoutRow()

    /** calculate view positions, view won't be actually added to layout when calling this method
     * @return true if view successfully placed, false if view can't be placed because out of space on screen and have to be recycled
     */
    fun placeView(view: View): Boolean

    /** Read layouter state from current attached view. We need only last of it, but we can't determine here which is last.
     * Based on characteristics of last attached view, layouter algorithm will be able to continue placing from it.
     * This method have to be called on attaching view
     * @return * @return true if view successfully attached, false if view can't be attached because out of space on screen
     */
    fun onAttachView(view: View): Boolean

    /** @return size of current row
     */
    fun getRowSize(): Int

    /** @return top of current row
     */
    fun getViewTop(): Int

    /** @return bottom of current row
     */
    fun getViewBottom(): Int

    /** @return size of previous row
     */
    fun getPreviousRowSize(): Int

    fun getCurrentRowItems(): ArrayList<Item>

    fun getRowRect(): Rect

    fun addLayouterListener(layouterListener: ILayouterListener?)

    fun removeLayouterListener(layouterListener: ILayouterListener?)

    fun positionIterator(): AbstractPositionIterator
}