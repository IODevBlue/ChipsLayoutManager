package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.util.GravityUtil.getVerticalDifference
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item


class ColumnFillSpaceCenterStrategy: IRowStrategy {

    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        val difference =
            getVerticalDifference(abstractLayouter) / (abstractLayouter.getRowSize() + 1)
        var offsetDifference = 0
        for (item in row) {
            val childRect = item.viewRect
            offsetDifference += difference
            childRect.top += offsetDifference
            childRect.bottom += offsetDifference
        }
    }
}