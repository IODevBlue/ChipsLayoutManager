package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker


/** this is basis row breaker for [com.blueiobase.api.android.chiplayoutmanager.layouter.RTLDownLayouter]  */
class RTLForwardRowBreaker : ILayoutRowBreaker {
    override fun isRowBroke(abstractLayouter: AbstractLayouter): Boolean {
        return (abstractLayouter.viewRight < abstractLayouter.getCanvasRightBorder()
                && abstractLayouter.viewRight - abstractLayouter.currentViewWidth < abstractLayouter.getCanvasLeftBorder())
    }
}
