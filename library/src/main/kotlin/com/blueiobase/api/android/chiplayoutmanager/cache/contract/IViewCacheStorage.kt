package com.blueiobase.api.android.chiplayoutmanager.cache.contract

import android.graphics.Rect
import android.os.Parcelable
import android.view.View


interface IViewCacheStorage {

    fun isPositionEndsRow(position: Int): Boolean

    fun isPositionStartsRow(position: Int): Boolean

    fun setCachingEnabled(isEnabled: Boolean)

    fun isCachingEnabled(): Boolean

    fun getStartOfRow(endRow: Int): Int

    fun storeRow(row: ArrayList<Pair<Rect, View>>)

    fun isInCache(position: Int): Boolean

    /** purge whole cache */
    fun purge()

    /** all cache to selected position will be purged
     * @param position the end position, exclusive
     */
    fun purgeCacheToPosition(position: Int)

    fun getLastCachePosition(): Int?

    fun isCacheEmpty(): Boolean

    /** all cache from selected position will be purged
     * @param position the start position, inclusive
     */
    fun purgeCacheFromPosition(position: Int)

    /** onSaveInstanceState cache storage content to [Parcelable] */
    fun onSaveInstanceState(): Parcelable

    fun onRestoreInstanceState( parcelable: Parcelable?)
}
