package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.util.GravityUtil.getHorizontalDifference
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item

class LTRRowFillStrategy: IRowStrategy {
    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        val difference = getHorizontalDifference(abstractLayouter) / abstractLayouter.getRowSize()
        var offsetDifference = difference

        for (item in row) {
            val childRect = item.viewRect
            if (childRect.left == abstractLayouter.getCanvasLeftBorder()) {
                //left view of row
                val leftDif: Int = childRect.left - abstractLayouter.getCanvasLeftBorder()
                //press view to left border
                childRect.left = abstractLayouter.getCanvasLeftBorder()
                childRect.right -= leftDif

                //increase view width from right
                childRect.right += offsetDifference
                continue
            }
            childRect.left += offsetDifference
            offsetDifference += difference
            childRect.right += offsetDifference
        }

    }
}