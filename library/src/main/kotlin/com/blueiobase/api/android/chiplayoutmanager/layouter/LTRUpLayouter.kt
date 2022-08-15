package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouter


class LTRUpLayouter private constructor(builder: Builder) :
    AbstractLayouter(builder), ILayouter {

    override fun createViewRect(view: View): Rect {
        val left = viewRight - currentViewWidth
        val viewTop = getViewBottom() - currentViewHeight
        val viewRect = Rect(left, viewTop, viewRight, getViewBottom())
        viewRight = viewRect.left
        return viewRect
    }

    override fun isReverseOrder(): Boolean {
        return true
    }

    override fun onPreLayout() {
        val leftOffsetOfRow = viewRight - getCanvasLeftBorder()
        viewLeft = 0
        for (rowViewRectPair in rowViews) {
            val viewRect = rowViewRectPair.first
            viewRect.left = viewRect.left - leftOffsetOfRow
            viewRect.right = viewRect.right - leftOffsetOfRow
            viewLeft = viewRect.right.coerceAtLeast(viewLeft)
            setViewTop(getViewTop().coerceAtMost(viewRect.top))
            setViewBottom(getViewBottom().coerceAtLeast(viewRect.bottom))
        }
    }

    override fun onAfterLayout() {
        //go to next row, increase top coordinate, reset left
        viewRight = getCanvasRightBorder()
        setViewBottom(getViewTop())
    }

    override fun isAttachedViewFromNewRow(view: View): Boolean {
        val bottomOfCurrentView = layoutManager.getDecoratedBottom(view)
        val rightOfCurrentView = layoutManager.getDecoratedRight(view)
        return (getViewTop() >= bottomOfCurrentView
                && rightOfCurrentView > viewRight)
    }

    override fun onInterceptAttachView(view: View) {
        if (viewRight != getCanvasRightBorder() && viewRight - currentViewWidth < getCanvasLeftBorder()) {
            //new row
            viewRight = getCanvasRightBorder()
            setViewBottom(getViewTop())
        } else {
            viewRight = layoutManager.getDecoratedLeft(view)
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
        return getCanvasRightBorder() - viewRight
    }


    companion object {
        class Builder : AbstractLayouter.Companion.Builder() {

            override fun createLayouter(): LTRUpLayouter {
                return LTRUpLayouter(this)
            }
        }

        fun newBuilder(): Builder {
            return Builder()
        }
    }
}
