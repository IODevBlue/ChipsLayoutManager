package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

/**
 * Base class which functions as a tag and exists mainly to group all classes which intend to implement the [ILayoutRowBreaker] interface.
 *
 * For uniformity, this class can be extended if a custom [ILayoutRowBreaker] implementation is needed.
 *
 * @author IO DevBlue
 * @since 1.0.0
 */
open class RowBreakerDecorator(private val decorate: ILayoutRowBreaker): ILayoutRowBreaker {
    override fun isRowBroke(abstractLayouter: AbstractLayouter) = decorate.isRowBroke(abstractLayouter)
}