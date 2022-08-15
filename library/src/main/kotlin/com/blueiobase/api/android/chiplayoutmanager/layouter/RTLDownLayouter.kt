package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import android.view.View


class RTLDownLayouter private constructor(builder: Builder) : AbstractLayouter(builder) {

    private var isPurged = false

    override fun onPreLayout() {
        if (rowViews.isNotEmpty()) {
            //todo this isn't great place for that. Should be refactored somehow
            if (!isPurged) {
                isPurged = true
                cacheStorage.purgeCacheFromPosition(layoutManager.getPosition(rowViews[0].second))
            }
            cacheStorage.storeRow(rowViews)
        }
    }

    override fun onAfterLayout() {
        //go to next row, increase top coordinate, reset left
        viewRight = getCanvasRightBorder()
        setViewTop(getViewBottom())
        //viewTop = viewBottom
    }

    override fun isAttachedViewFromNewRow(view: View): Boolean {
        val topOfCurrentView = layoutManager.getDecoratedTop(view)
        val rightOfCurrentVIew = layoutManager.getDecoratedRight(view)
        return (getViewBottom() <= topOfCurrentView
                && rightOfCurrentVIew > viewRight)
    }

    override fun createViewRect(view: View): Rect {
        val viewRect = Rect(viewRight - currentViewWidth, getViewTop(), viewRight, getViewTop() + currentViewHeight)
        viewRight = viewRect.left
//        viewBottom = viewBottom.coerceAtLeast(viewRect.bottom)
        setViewBottom(getViewBottom().coerceAtLeast(viewRect.bottom))
        return viewRect
    }

    override fun isReverseOrder() = false

    override fun onInterceptAttachView(view: View) {
        setViewTop(layoutManager.getDecoratedTop(view))
        viewRight = layoutManager.getDecoratedLeft(view)
        setViewBottom(getViewBottom().coerceAtLeast(layoutManager.getDecoratedBottom(view)))
    }

    override fun getStartRowBorder(): Int {
        return getViewTop()
    }

    override fun getEndRowBorder(): Int {
        return getViewBottom()
    }

    override fun getRowLength(): Int {
        return getCanvasRightBorder() - viewRight
    }


    companion object {
        class Builder: AbstractLayouter.Companion.Builder() {

            override fun createLayouter(): RTLDownLayouter {
                return RTLDownLayouter(this)
            }
        }
        fun newBuilder(): Builder {
            return Builder()
        }
    }
}