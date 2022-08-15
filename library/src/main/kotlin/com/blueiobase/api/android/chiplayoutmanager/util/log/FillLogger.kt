package com.blueiobase.api.android.chiplayoutmanager.util.log

import android.util.SparseArray
import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import java.util.*
import kotlin.Int

 class FillLogger(viewCache: SparseArray<View>) : IFillLogger {
    private val viewCache: SparseArray<View>
    private var requestedItems = 0
    private var recycledItems = 0
    private var startCacheSize = 0
    private var recycledSize = 0
    override fun onStartLayouter(startPosition: Int) {
        requestedItems = 0
        recycledItems = 0
        startCacheSize = viewCache.size()
        Log.d("fillWithLayouter", "start position = $startPosition", LogSwitcherFactory.FILL)
        Log.d("fillWithLayouter", "cached items = $startCacheSize", LogSwitcherFactory.FILL)
    }

    override fun onItemRequested() {
        requestedItems++
    }

    override fun onItemRecycled() {
        recycledItems++
    }

    override fun onFinishedLayouter() {
        Log.d(
            "fillWithLayouter",
            String.format(
                Locale.getDefault(),
                "reattached items = %d : requested items = %d recycledItems = %d",
                startCacheSize - viewCache.size(),
                requestedItems,
                recycledItems
            ),
            LogSwitcherFactory.FILL
        )
    }

    override fun onAfterLayouter() {
        recycledSize = viewCache.size()
    }

    override fun onRemovedAndRecycled(position: Int) {
        Log.d(
            "fillWithLayouter",
            " recycle position =" + viewCache.keyAt(position),
            LogSwitcherFactory.FILL
        )
        recycledSize++
    }

    override fun onAfterRemovingViews() {
        Log.d("fillWithLayouter", "recycled count = $recycledSize", LogSwitcherFactory.FILL)
    }

    override fun onBeforeLayouter(state: AnchorViewState?) {
        state!!.anchorViewRect?.let {
            Log.d("fill", "anchorPos " + state.position, LogSwitcherFactory.FILL)
            Log.d("fill", "anchorTop " + state.anchorViewRect!!.top, LogSwitcherFactory.FILL)
        }
    }

    init {
        this.viewCache = viewCache
    }
}