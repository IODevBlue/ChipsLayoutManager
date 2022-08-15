package com.blueiobase.api.android.chiplayoutmanager.layouter.contract

import android.graphics.Rect
import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IBorder

interface ICanvas: IBorder {

    fun getCanvasRect(): Rect

    fun getViewRect(view: View): Rect

    fun isInside(rectCandidate: Rect): Boolean

    fun isInside(viewCandidate: View): Boolean

    fun isFullyVisible(view: View): Boolean

    fun isFullyVisible(rect: Rect): Boolean

    /** calculate border state of layout manager after filling children */
    fun findBorderViews()

    fun getTopView(): View?

    fun getBottomView(): View?

    fun getLeftView(): View?

    fun getRightView(): View?

    fun getMinPositionOnScreen(): Int?

    fun getMaxPositionOnScreen(): Int?

    fun isFirstItemAdded(): Boolean
}