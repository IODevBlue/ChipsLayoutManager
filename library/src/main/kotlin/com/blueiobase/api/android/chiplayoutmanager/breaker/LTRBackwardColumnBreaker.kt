package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

class LTRBackwardColumnBreaker: ILayoutRowBreaker {

    override fun isRowBroke(abstractLayouter: AbstractLayouter): Boolean {
        return (abstractLayouter.getViewBottom() - abstractLayouter.currentViewHeight < abstractLayouter.getCanvasTopBorder()
                && abstractLayouter.getViewBottom() < abstractLayouter.getCanvasBottomBorder())
    }
}