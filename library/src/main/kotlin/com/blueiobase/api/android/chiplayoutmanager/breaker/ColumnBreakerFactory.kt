package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager

/**
 * This class is responsible for breaking the [View] object anchored at the extreme ends of the
 * [ChipsLayoutManager] when the layout orientation is [vertical][ChipsLayoutManager.VERTICAL].
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
class ColumnBreakerFactory: IBreakerFactory {

    override fun createBackwardRowBreaker() = LTRBackwardColumnBreaker()

    override fun createForwardRowBreaker() = LTRForwardColumnBreaker()

}