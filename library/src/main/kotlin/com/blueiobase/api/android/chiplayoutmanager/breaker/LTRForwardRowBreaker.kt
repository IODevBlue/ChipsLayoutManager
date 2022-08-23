package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

class LTRForwardRowBreaker: ILayoutRowBreaker {

    override fun isRowBroke(abstractLayouter: AbstractLayouter): Boolean {
        return (abstractLayouter.viewLeft > abstractLayouter.getCanvasLeftBorder()
                && abstractLayouter.viewLeft + abstractLayouter.currentViewWidth > abstractLayouter.getCanvasRightBorder())
    }
}