package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.cache.contract.IViewCacheStorage
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker


class CacheRowBreaker(private val cacheStorage: IViewCacheStorage, decorate: ILayoutRowBreaker): RowBreakerDecorator(decorate) {

    override fun isRowBroke(al: AbstractLayouter): Boolean {
        val stopDueToCache = cacheStorage.isPositionEndsRow(al.currentViewPosition)
        return super.isRowBroke(al) || stopDueToCache
    }
}