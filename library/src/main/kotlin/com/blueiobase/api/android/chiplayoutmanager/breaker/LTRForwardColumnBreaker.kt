package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

class LTRForwardColumnBreaker: ILayoutRowBreaker {

    override fun isRowBroke(abstractLayouter: AbstractLayouter): Boolean {
        return (abstractLayouter.getViewTop() > abstractLayouter.getCanvasTopBorder()
                && abstractLayouter.getViewTop() + abstractLayouter.currentViewHeight > abstractLayouter.getCanvasBottomBorder())
    }
}