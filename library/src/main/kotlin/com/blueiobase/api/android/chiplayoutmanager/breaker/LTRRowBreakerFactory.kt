package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager


/**
 * This class provides the appropriate delegate to handle the breaking of [View] objects anchored at the extreme ends
 * of the [ChipsLayoutManager] when the row configuration is in Left-To-Right.
 *
 * @author IO DevBlue
 * @since 1.0.0
 */
class LTRRowBreakerFactory: IBreakerFactory {

    override fun createBackwardRowBreaker() = LTRBackwardRowBreaker()

    override fun createForwardRowBreaker() = LTRForwardRowBreaker()

}