package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.util.GravityUtil.getVerticalDifference
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item


class ColumnFillStrategy: IRowStrategy {

    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        val difference = getVerticalDifference(abstractLayouter) / abstractLayouter.getRowSize()
        var offsetDifference = difference
        for (item in row) {
            val childRect = item.viewRect
            if (childRect.top == abstractLayouter.getCanvasTopBorder()) {
                //highest view of row
                val topDif: Int = childRect.top - abstractLayouter.getCanvasTopBorder()
                //press view to top border
                childRect.top = abstractLayouter.getCanvasTopBorder()
                childRect.bottom -= topDif

                //increase view height from bottom
                childRect.bottom += offsetDifference
                continue
            }
            childRect.top += offsetDifference
            offsetDifference += difference
            childRect.bottom += offsetDifference
        }
    }
}