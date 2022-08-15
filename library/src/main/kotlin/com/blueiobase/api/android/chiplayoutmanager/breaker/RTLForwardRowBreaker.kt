package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker


/** this is basis row breaker for [com.blueiobase.api.android.chiplayoutmanager.layouter.RTLDownLayouter]  */
class RTLForwardRowBreaker : ILayoutRowBreaker {
    override fun isRowBroke(al: AbstractLayouter): Boolean {
        return (al.viewRight < al.getCanvasRightBorder()
                && al.viewRight - al.currentViewWidth < al.getCanvasLeftBorder())
    }
}
