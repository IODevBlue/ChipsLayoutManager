package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IRowBreaker

class BackwardBreakerContract(private val breaker: IRowBreaker, decorate: ILayoutRowBreaker): RowBreakerDecorator(decorate) {

    override fun isRowBroke(al: AbstractLayouter): Boolean {
        return super.isRowBroke(al) || breaker.isItemBreakRow(al.currentViewPosition)
    }
}