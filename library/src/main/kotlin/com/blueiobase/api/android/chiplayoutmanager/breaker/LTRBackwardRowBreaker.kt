package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

class LTRBackwardRowBreaker: ILayoutRowBreaker {

    override fun isRowBroke(abstractLayouter: AbstractLayouter): Boolean {
        return (abstractLayouter.viewRight - abstractLayouter.currentViewWidth < abstractLayouter.getCanvasLeftBorder()
                && abstractLayouter.viewRight < abstractLayouter.getCanvasRightBorder())
    }
}