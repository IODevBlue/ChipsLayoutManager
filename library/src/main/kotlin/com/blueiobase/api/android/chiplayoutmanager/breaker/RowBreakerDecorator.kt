package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

open class RowBreakerDecorator(private val decorate: ILayoutRowBreaker): ILayoutRowBreaker {
    override fun isRowBroke(al: AbstractLayouter) = decorate.isRowBroke(al)
}