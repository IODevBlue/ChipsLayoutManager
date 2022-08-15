package com.blueiobase.api.android.chiplayoutmanager.util

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter


object GravityUtil {
    fun getHorizontalDifference(layouter: AbstractLayouter) = layouter.getCanvasRightBorder() - layouter.getCanvasLeftBorder() - layouter.getRowLength()

    fun getVerticalDifference(layouter: AbstractLayouter) = layouter.getCanvasBottomBorder() - layouter.getCanvasTopBorder() - layouter.getRowLength()

}