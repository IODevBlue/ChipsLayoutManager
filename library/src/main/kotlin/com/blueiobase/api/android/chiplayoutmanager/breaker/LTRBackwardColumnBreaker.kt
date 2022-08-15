package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

class LTRBackwardColumnBreaker: ILayoutRowBreaker {

    override fun isRowBroke(al: AbstractLayouter): Boolean {
        return (al.getViewBottom() - al.currentViewHeight < al.getCanvasTopBorder()
                && al.getViewBottom() < al.getCanvasBottomBorder())
    }
}