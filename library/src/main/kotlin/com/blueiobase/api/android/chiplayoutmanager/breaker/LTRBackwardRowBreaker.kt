package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

class LTRBackwardRowBreaker: ILayoutRowBreaker {

    override fun isRowBroke(al: AbstractLayouter): Boolean {
        return (al.viewRight - al.currentViewWidth < al.getCanvasLeftBorder()
                && al.viewRight < al.getCanvasRightBorder())
    }
}