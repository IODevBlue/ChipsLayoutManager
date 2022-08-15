package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

class LTRForwardColumnBreaker: ILayoutRowBreaker {

    override fun isRowBroke(al: AbstractLayouter): Boolean {
        return (al.getViewTop() > al.getCanvasTopBorder()
                && al.getViewTop() + al.currentViewHeight > al.getCanvasBottomBorder())
    }
}