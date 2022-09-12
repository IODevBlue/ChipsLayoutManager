package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.cache.contract.IViewCacheStorage
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager

/**
 * This class is mainly responsible for the handling and breaking/splicing of [View] objects at the
 * extreme ends of the [ChipsLayoutManager] when the layout orientation is [horizontal][ChipsLayoutManager.HORIZONTAL]..
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
class DecoratorBreakerFactory(
    private val cacheStorage: IViewCacheStorage,
    private val rowBreaker: IRowBreaker,
    private val maxViewsInRow: Int?,
    private val breakerFactory: IBreakerFactory
) : IBreakerFactory {

    override fun createBackwardRowBreaker(): ILayoutRowBreaker {
        var breaker = breakerFactory.createBackwardRowBreaker()
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