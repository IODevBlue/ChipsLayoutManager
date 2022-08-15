package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IRowBreaker


class ForwardBreakerContract(private val breaker: IRowBreaker, decorate: ILayoutRowBreaker) : RowBreakerDecorator(decorate) {

    override fun isRowBroke(al: AbstractLayouter) = super.isRowBroke(al) ||
            al.currentViewPosition != 0 && breaker.isItemBreakRow(al.currentViewPosition - 1)

}