package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker


/** brakes the row in case max views size in row reached  */
class MaxViewsBreaker (private val maxViewsInRow: Int, decorate: ILayoutRowBreaker): RowBreakerDecorator(decorate) {

    override fun isRowBroke(al: AbstractLayouter) = super.isRowBroke(al) || al.getRowSize() >= maxViewsInRow

}