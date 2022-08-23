package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IRowBreaker

/**
 * A [RowBreakerDecorator] which breaks/splices [View] objects at the extreme left/start of a layout.
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
class ForwardBreakerContract(private val breaker: IRowBreaker, decorate: ILayoutRowBreaker) : RowBreakerDecorator(decorate) {

    override fun isRowBroke(abstractLayouter: AbstractLayouter) = abstractLayouter.let {
        super.isRowBroke(it) || it.currentViewPosition != 0 && breaker.isItemBreakRow(it.currentViewPosition - 1)
    }

}