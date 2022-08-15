package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.cache.contract.IViewCacheStorage
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IRowBreaker


class DecoratorBreakerFactory(
    private val cacheStorage: IViewCacheStorage,
    private val rowBreaker: IRowBreaker,
    /** Max items in row restriction. Layout of row should be stopped when this count of views reached */
    private val maxViewsInRow: Int?, private val breakerFactory: IBreakerFactory
) : IBreakerFactory {

    override fun createBackwardRowBreaker(): ILayoutRowBreaker {
        var breaker: ILayoutRowBreaker = breakerFactory.createBackwardRowBreaker()
        breaker = BackwardBreakerContract(rowBreaker, CacheRowBreaker(cacheStorage, breaker))
        maxViewsInRow?.let{
            breaker = MaxViewsBreaker(it, breaker)
        }
        return breaker
    }

    override fun createForwardRowBreaker(): ILayoutRowBreaker {
        var breaker = breakerFactory.createForwardRowBreaker()
        breaker = ForwardBreakerContract(rowBreaker, breaker)
        maxViewsInRow?.let{
            breaker = MaxViewsBreaker(it, breaker)
        }
        return breaker
    }
}