package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import android.view.View


internal class LTRDownLayouter private constructor(builder: Builder) :
    AbstractLayouter(builder) {

    private var isPurged = false
    override fun createViewRect(view: View): Rect {
        val viewRect =
            Rect(viewLeft, getViewTop(), viewLeft + currentViewWidth, getViewTop() + currentViewHeight)
        viewLeft = viewRect.right
        setViewBottom(getViewBottom().coerceAtLeast(viewRect.bottom))
        return viewRect
    }

    override fun isReverseOrder(): Boolean {
        return false
    }

    override fun onPreLayout() {
        if (rowViews.isNotEmpty()) {
            //todo this isn't great place for that. Should be refactored somehow
            if (!isPurged) {
                isPurged = true
                cacheStorage.purgeCacheFromPosition(layoutManager.getPosition(rowViews[0].second))
            }

            //cache only when go down
            cacheStorage.storeRow(rowViews)
        }
    }

    override fun onAfterLayout() {
        //go to next row, increase top coordinate, reset left
        viewLeft = getCanvasLeftBorder()
        setViewTop(getViewBottom())
    }

    override fun isAttachedViewFromNewRow(view: View): Boolean {
        val topOfCurrentView = layoutManager.getDecoratedTop(view)
        val leftOfCurrentView = layoutManager.getDecoratedLeft(view)
        return (getViewBottom() <= topOfCurrentView
                && leftOfCurrentView < viewLeft)
    }

    override fun onInterceptAttachView(view: View) {
        setViewTop(layoutManager.getDecoratedTop(view))
        viewLeft = layoutManager.getDecoratedRight(view)
        setViewBottom(getViewBottom().coerceAtLeast(layoutManager.getDecoratedBottom(view)))
    }

    override fun getStartRowBorder(): Int {
        return getViewTop()
    }

    override fun getEndRowBorder(): Int {
        return getViewBottom()
    }

    override fun getRowLength(): Int {
        return viewLeft - getCanvasLeftBorder()
    }

    class Builder : AbstractLayouter.Companion.Builder() {
        override fun createLayouter(): LTRDownLayouter {
            return LTRDownLayouter(this)
        }
    }

    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
    }
}
