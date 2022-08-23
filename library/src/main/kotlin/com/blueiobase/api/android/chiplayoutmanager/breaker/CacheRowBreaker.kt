package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.cache.contract.IViewCacheStorage
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

/**
 * A [RowBreakerDecorator] which breaks/splices [View] objects saved with the internal Caching mechanism.
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
class CacheRowBreaker(private val cacheStorage: IViewCacheStorage, decorate: ILayoutRowBreaker): RowBreakerDecorator(decorate) {

    override fun isRowBroke(abstractLayouter: AbstractLayouter) = abstractLayouter.let {
        super.isRowBroke(abstractLayouter) || cacheStorage.isPositionEndsRow(it.currentViewPosition)
    }
}