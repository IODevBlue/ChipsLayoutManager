package com.blueiobase.api.android.chiplayoutmanager.breaker.contract

import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager
import android.view.View

/**
 * Interface for the creation of [ILayoutRowBreaker] implementations by providing factory methods to support
 * breaking/splicing of [View] objects either at the start or end of a [ChipsLayoutManager].
 *
 * @author IO DevBlue
 * @since 1.0.0
 */
interface IBreakerFactory {

    /**
     * Creates an [ILayoutRowBreaker] implementation which breaks/splices [View] objects
     * at the extreme right/end of a [ChipsLayoutManager].
     *
     * @return An [ILayoutRowBreaker] implementation.
     */
    fun createBackwardRowBreaker(): ILayoutRowBreaker

    /**
     * Creates an [ILayoutRowBreaker] implementation which breaks/splices [View] objects
     * at the extreme left/start of a [ChipsLayoutManager].
     *
     * @return An [ILayoutRowBreaker] implementation.
     */

    fun createForwardRowBreaker(): ILayoutRowBreaker
}