package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import android.view.View


class RightLayouter private constructor(builder: Builder) :
    AbstractLayouter(builder) {

    private var isPurged = false

    override fun createViewRect(view: View): Rect {
        val viewRect =
            Rect(viewLeft, getViewTop(), viewLeft + currentViewWidth, getViewTop() + currentViewHeight)
        setViewBottom(viewRect.bottom)
        setViewTop(getViewBottom())
        viewRight = viewRight.coerceAtLeast(viewRect.right)
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
        //go to next column, increase left coordinate, reset top
        viewLeft = viewRight
        setViewTop(getCanvasTopBorder())
    }

    override fun isAttachedViewFromNewRow(view: View): Boolean {
        val leftOfCurrentView = layoutManager.getDecoratedLeft(view)
        val topOfCurrentView = layoutManager.getDecoratedTop(view)
        return (viewRight <= leftOfCurrentView
                && topOfCurrentView < getViewTop())
    }

    override fun onInterceptAttachView(view: View) {
        setViewTop(layoutManager.getDecoratedBottom(view))
        viewLeft = layoutManager.getDecoratedLeft(view)
        viewRight = viewRight.coerceAtLeast(layoutManager.getDecoratedRight(view))
    }

    override fun getStartRowBorder(): Int {
        return viewLeft
    }

    override fun getEndRowBorder(): Int {
        return viewRight
    }

    override fun getRowLength(): Int {
        return getViewTop() - getCanvasTopBorder()
    }

    companion object {

        class Builder : AbstractLayouter.Companion.Builder() {
            override fun createLayouter(): RightLayouter {
                return RightLayouter(this)
            }
        }

        fun newBuilder(): Builder {
            return Builder()
        }
    }
}
