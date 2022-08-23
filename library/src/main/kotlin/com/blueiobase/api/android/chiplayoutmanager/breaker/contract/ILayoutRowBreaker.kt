package com.blueiobase.api.android.chiplayoutmanager.breaker.contract

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter

/**
 * Interface to decide if a row has been broken from the specified [AbstractLayouter] implementation.
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
interface ILayoutRowBreaker {

    /**
     * Validates if the provided [abstractLayouter] has a broken row.
     * @param abstractLayouter The [AbstractLayouter] which manages the positioning of [View] objects.
     * @return `true` if the [AbstractLayouter] has a broken row. `false` if otherwise.
     *
     */
    fun isRowBroke(abstractLayouter: AbstractLayouter): Boolean
}