package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.util.GravityUtil.getVerticalDifference
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item


class ColumnFillSpaceStrategy: IRowStrategy {
    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        if (abstractLayouter.getRowSize() == 1) return
        val difference =
            getVerticalDifference(abstractLayouter) / (abstractLayouter.getRowSize() - 1)
        var offsetDifference = 0
        for (item in row) {
            val childRect = item.viewRect
            if (childRect.top == abstractLayouter.getCanvasTopBorder()) {
                //highest view of row
                val topDif: Int = childRect.top - abstractLayouter.getCanvasTopBorder()
                //press view to top border
                childRect.top = abstractLayouter.getCanvasTopBorder()
                childRect.bottom -= topDif
                continue
            }
            offsetDifference += difference
            childRect.top += offsetDifference
            childRect.bottom += offsetDifference
        }

    }

}