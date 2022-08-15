package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.util.GravityUtil.getHorizontalDifference
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item


class LTRRowFillSpaceStrategy: IRowStrategy {

    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        if (abstractLayouter.getRowSize() == 1) return
        val difference = getHorizontalDifference(abstractLayouter) / (abstractLayouter.getRowSize() - 1)
        var offsetDifference = 0
        for (item in row) {
            val childRect = item.viewRect
            if (childRect.left == abstractLayouter.getCanvasLeftBorder()) {
                //left view of row
                val leftDif: Int = childRect.left - abstractLayouter.getCanvasLeftBorder()
                //press view to left border
                childRect.left = abstractLayouter.getCanvasLeftBorder()
                childRect.right -= leftDif
                continue
            }
            offsetDifference += difference
            childRect.left += offsetDifference
            childRect.right += offsetDifference
        }
    }
}