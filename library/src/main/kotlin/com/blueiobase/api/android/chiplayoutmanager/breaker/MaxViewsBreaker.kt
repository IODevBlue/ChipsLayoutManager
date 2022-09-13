package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker


/**
 * A [RowBreakerDecorator] which breaks/splices [View] objects when the maximum amount of [View] objects
 * the layout can contain has reached its limit.
 *
 * @author IO DevBlue
 * @since 1.0.0
 **/
class MaxViewsBreaker (private val maxViewsInRow: Int, decorate: ILayoutRowBreaker): RowBreakerDecorator(decorate) {

    override fun isRowBroke(abstractLayouter: AbstractLayouter) = super.isRowBroke(abstractLayouter) || abstractLayouter.getRowSize() >= maxViewsInRow

}