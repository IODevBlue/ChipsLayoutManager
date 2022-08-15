package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory

class ColumnBreakerFactory: IBreakerFactory {

    override fun createBackwardRowBreaker() = LTRBackwardColumnBreaker()

    override fun createForwardRowBreaker() = LTRForwardColumnBreaker()

}