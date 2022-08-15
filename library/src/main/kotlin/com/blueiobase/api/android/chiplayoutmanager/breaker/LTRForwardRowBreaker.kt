package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

class LTRForwardRowBreaker: ILayoutRowBreaker {

    override fun isRowBroke(al: AbstractLayouter): Boolean {
        return (al.viewLeft > al.getCanvasLeftBorder()
                && al.viewLeft + al.currentViewWidth > al.getCanvasRightBorder())
    }
}