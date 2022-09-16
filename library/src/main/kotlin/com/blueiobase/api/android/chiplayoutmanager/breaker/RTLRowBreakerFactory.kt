package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager

/**
 * This class provides the appropriate delegate to handle the breaking of [View] objects anchored at the extreme ends
 * of the [ChipsLayoutManager] when the row configuration is in Right-To-Left.
 *
 * @author IO DevBlue
 * @since 1.0.0
 */
class RTLRowBreakerFactory : IBreakerFactory {
    override fun createBackwardRowBreaker() =  RTLBackwardRowBreaker()

    override fun createForwardRowBreaker() = RTLForwardRowBreaker()

}