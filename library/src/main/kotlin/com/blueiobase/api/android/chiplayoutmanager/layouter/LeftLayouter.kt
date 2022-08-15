package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import android.view.View


class LeftLayouter private constructor(builder: Builder) :
    AbstractLayouter(builder) {

    override fun createViewRect(view: View): Rect {
        val left = viewRight - currentViewWidth
        val viewTop = getViewBottom() - currentViewHeight
        val viewRect = Rect(left, viewTop, viewRight, getViewBottom())
        setViewBottom(viewRect.top)
        return viewRect
    }

    override fun isReverseOrder(): Boolean {
        return true
    }

    override fun onPreLayout() {
        val topOffsetOfRow = getViewBottom() - getCanvasTopBorder()
        setViewBottom(0)

        for (columnViewRectPair in rowViews) {
            val viewRect = columnViewRectPair.first
            viewRect.top = viewRect.top - topOffsetOfRow
            viewRect.bottom = viewRect.bottom - topOffsetOfRow
            setViewBottom(getViewBottom().coerceAtLeast(viewRect.bottom))
            viewLeft = viewLeft.coerceAtMost(viewRect.left)
            viewRight = viewRight.coerceAtLeast(viewRect.right)
        }
    }

    override fun onAfterLayout() {
        setViewBottom(getCanvasBottomBorder())
        viewRight = viewLeft
    }

    override fun isAttachedViewFromNewRow(view: View): Boolean {
        val bottomOfCurrentView = layoutManager.getDecoratedBottom(view)
        val rightOfCurrentView = layoutManager.getDecoratedRight(view)
        return (viewLeft >= rightOfCurrentView
                && bottomOfCurrentView > getViewBottom())
    }

    override fun onInterceptAttachView(view: View) {
        if (getViewBottom() != getCanvasBottomBorder() && getViewBottom() - currentViewHeight < getCanvasTopBorder()) {
            //new column
            setViewBottom(getCanvasBottomBorder())
            viewRight = viewLeft
        } else {
            setViewBottom(layoutManager.getDecoratedTop(view))
        }
        viewLeft = viewLeft.coerceAtMost(layoutManager.getDecoratedLeft(view))
    }

    override fun getStartRowBorder(): Int {
        return viewLeft
    }

    override fun getEndRowBorder(): Int {
        return viewRight
    }

    override fun getRowLength(): Int {
        return getViewBottom() - getCanvasTopBorder()
    }

    companion object {

        class Builder : AbstractLayouter.Companion.Builder() {
            override fun createLayouter(): LeftLayouter {
                return LeftLayouter(this)
            }
        }

        fun newBuilder(): Builder {
            return Builder()
        }
    }
}
