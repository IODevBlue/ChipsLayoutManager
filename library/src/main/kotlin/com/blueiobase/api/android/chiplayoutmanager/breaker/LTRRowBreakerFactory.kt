package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory

class LTRRowBreakerFactory: IBreakerFactory {

    override fun createBackwardRowBreaker() = LTRBackwardRowBreaker()

    override fun createForwardRowBreaker() = LTRForwardRowBreaker()

}