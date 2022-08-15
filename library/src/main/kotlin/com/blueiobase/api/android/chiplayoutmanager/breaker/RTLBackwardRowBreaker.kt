package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

/** this is basis row breaker for [com.blueiobase.api.android.chiplayoutmanager.layouter.RTLUpLayouter]  */
class RTLBackwardRowBreaker : ILayoutRowBreaker {
    override fun isRowBroke(al: AbstractLayouter) = al.viewLeft + al.currentViewWidth > al.getCanvasRightBorder()
                && al.viewLeft > al.getCanvasLeftBorder()

}