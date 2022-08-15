package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.util.GravityUtil.getHorizontalDifference
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item

class RTLRowFillSpaceStrategy: IRowStrategy {
    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        if (abstractLayouter.getRowSize() == 1) return
        val difference = getHorizontalDifference(abstractLayouter) / (abstractLayouter.getRowSize() - 1)
        var offsetDifference = 0

        for (item in row) {
            val childRect = item.viewRect
            if (childRect.right == abstractLayouter.getCanvasRightBorder()) {
                //right view of row
                val rightDif: Int = abstractLayouter.getCanvasRightBorder() - childRect.right
                //press view to right border
                childRect.left += rightDif
                childRect.right = abstractLayouter.getCanvasRightBorder()
                continue
            }
            offsetDifference += difference
            childRect.right -= offsetDifference
            childRect.left -= offsetDifference
        }

    }
}