package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouter


class RTLUpLayouter private constructor(builder: Builder): AbstractLayouter(builder), ILayouter {

    companion object {

        fun newBuilder(): Builder {
            return Builder()
        }
    }

    override fun onPreLayout() {
        val leftOffsetOfRow = -(getCanvasRightBorder() - viewLeft)
        viewLeft = if (rowViews.size > 0) Int.MAX_VALUE else 0
        for (pair in rowViews) {
            val viewRect = pair.first
            viewRect.apply{
                left -= leftOffsetOfRow
                right -= leftOffsetOfRow
                viewLeft = viewLeft.coerceAtMost(left)
                setViewTop(getViewTop().coerceAtMost(top))
                setViewBottom(getViewBottom().coerceAtLeast(viewRect.bottom))
            }
        }
    }

    override fun onAfterLayout() {
        //go to next row, increase top coordinate, reset left
        viewLeft = getCanvasLeftBorder()
        setViewBottom(getViewTop())
    }

    override fun isAttachedViewFromNewRow(view: View): Boolean {
        val bottomOfCurrentView = layoutManager.getDecoratedBottom(view)
        val leftOfCurrentView = layoutManager.getDecoratedLeft(view)
        return (getViewTop() >= bottomOfCurrentView
                && leftOfCurrentView < viewLeft)
    }

    override fun createViewRect(view: View): Rect {
        val right = viewLeft + currentViewWidth
        val viewTop = getViewBottom() - currentViewHeight
        val viewRect = Rect(viewLeft, viewTop, right, getViewBottom())
        viewLeft = viewRect.right
        return viewRect
    }

    override fun isReverseOrder(): Boolean {
        return true
    }

    override fun onInterceptAttachView(view: View) {
        if (viewLeft != getCanvasLeftBorder() && viewLeft + currentViewWidth > getCanvasRightBorder()) {
            viewLeft = getCanvasLeftBorder()
            setViewBottom(getViewTop())
        } else {
            viewLeft = layoutManager.getDecoratedRight(view)
        }
        setViewTop(getViewTop().coerceAtMost(layoutManager.getDecoratedTop(view)))
    }

    override fun getStartRowBorder(): Int {
        return getViewTop()
    }

    override fun getEndRowBorder(): Int {
        return getViewBottom()
    }

    override fun getRowLength(): Int {
        return getCanvasRightBorder() - viewLeft
    }

    class Builder : AbstractLayouter.Companion.Builder() {

        override fun createLayouter(): RTLUpLayouter {
            return RTLUpLayouter(this)
        }
    }


}