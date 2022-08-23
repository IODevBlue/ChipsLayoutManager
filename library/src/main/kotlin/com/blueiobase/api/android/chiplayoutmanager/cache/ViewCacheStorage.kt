package com.blueiobase.api.android.chiplayoutmanager.cache

import android.graphics.Rect
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.cache.contract.IViewCacheStorage
import com.blueiobase.api.android.chiplayoutmanager.cache.model.CacheParcelableContainer
import java.util.*

class ViewCacheStorage(private val layoutManager: RecyclerView.LayoutManager) : IViewCacheStorage {

    companion object {
        private val TAG = ViewCacheStorage::class.java.simpleName
        private const val SIZE_MAX_CACHE = 1000

    }

    private var startsRow: NavigableSet<Int> = TreeSet()
    private var endsRow: NavigableSet<Int> = TreeSet()
    val maxCacheSize = SIZE_MAX_CACHE
    private var isCachingEnabled = false

    override fun isCachingEnabled() = isCachingEnabled

    override fun setCachingEnabled(isEnabled: Boolean) {
        if (isCachingEnabled == isEnabled) return
        Log.i(TAG, if (isEnabled) "caching enabled" else "caching disabled")
        isCachingEnabled = isEnabled
    }
    override fun getStartOfRow(endRow: Int): Int {
        var integer = startsRow.floor(endRow)
        if (integer == null) {
            integer = endRow
        }
        return integer
    }

    override fun isPositionEndsRow(position: Int) = endsRow.contains(position)

    override fun isPositionStartsRow(position: Int) = startsRow.contains(position)

    override fun storeRow(row: ArrayList<Pair<Rect, View>>) {
        if (isCachingEnabled && row.isNotEmpty()) {
            val (_, second) = row[0] //First pair
            val (_, second1) = row[row.size - 1] //Second pair
            val startPosition = layoutManager.getPosition(second)
            val endPosition = layoutManager.getPosition(second1)
            checkCacheSizeReached()
            startsRow.add(startPosition)
            endsRow.add(endPosition)
        }
    }

    override fun isInCache(position: Int) = startsRow.ceiling(position) != null || endsRow.ceiling(position) != null

    override fun purge() {
        startsRow.clear()
        endsRow.clear()
    }

    override fun purgeCacheToPosition(position: Int) {
        if (isCacheEmpty()) return
        Log.d(TAG, "cache purged to position $position")
        var removeIterator = startsRow.headSet(position).iterator()
        removeIterator.apply {
            while (hasNext()) {
                next(); remove()
            }
            removeIterator = endsRow.headSet(position).iterator()
            while (hasNext()) {
                next(); remove()
            }
        }

    }

    override fun getLastCachePosition() =  if (isCacheEmpty()) null else endsRow.last()

    override fun isCacheEmpty() = endsRow.isEmpty()

    override fun purgeCacheFromPosition(position: Int) {
        if (isCacheEmpty()) return
        var removeIterator = startsRow.tailSet(position, true).iterator()
        removeIterator.apply {
            while (hasNext()) {
                next(); remove()
            }
            var previous = startsRow.lower(position)
            previous = previous ?: position

            //we should also remove previous end row cache to guarantee consistency
            removeIterator = endsRow.tailSet(previous, true).iterator()
            while (hasNext()) {
                next(); remove()
            }
        }

    }

    override fun onSaveInstanceState() = CacheParcelableContainer(startsRow, endsRow)

    override fun onRestoreInstanceState(parcelable: Parcelable?) {
        if (parcelable == null) return
        check(parcelable is CacheParcelableContainer) { "wrong parcelable passed" }
        startsRow = parcelable.startsRow
        endsRow = parcelable.endsRow
    }

    //todo test max size cache reached
    private fun checkCacheSizeReached() {
        if (startsRow.size > maxCacheSize) {
            startsRow.remove(startsRow.first())
        }
        if (endsRow.size > maxCacheSize) {
            endsRow.remove(endsRow.first())
        }
    }


}