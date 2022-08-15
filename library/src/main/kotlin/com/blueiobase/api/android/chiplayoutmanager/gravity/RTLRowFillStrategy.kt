package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.util.GravityUtil.getHorizontalDifference
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item

class RTLRowFillStrategy: IRowStrategy {
    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        val difference = getHorizontalDifference(abstractLayouter) / abstractLayouter.getRowSize()
        var offsetDifference = difference

        for (item in row) {
            val childRect = item.viewRect
            if (childRect.right == abstractLayouter.getCanvasRightBorder()) {
                //right view of row
                val rightDif: Int = abstractLayouter.getCanvasRightBorder() - childRect.right
                //press view to right border
                childRect.left += rightDif
                childRect.right = abstractLayouter.getCanvasRightBorder()
                childRect.left -= offsetDifference
                continue
            }
            childRect.right -= offsetDifference
            offsetDifference += difference
            childRect.left -= offsetDifference
        }

    }
}