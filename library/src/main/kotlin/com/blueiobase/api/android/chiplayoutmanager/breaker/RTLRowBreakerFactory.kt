package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

class RTLRowBreakerFactory : IBreakerFactory {
    override fun createBackwardRowBreaker(): ILayoutRowBreaker {
        return RTLBackwardRowBreaker()
    }

    override fun createForwardRowBreaker(): ILayoutRowBreaker {
        return RTLForwardRowBreaker()
    }
}