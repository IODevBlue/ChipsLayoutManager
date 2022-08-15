package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.util.GravityUtil.getHorizontalDifference
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item

class LTRRowFillSpaceCenterStrategy: IRowStrategy {

    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        val difference = getHorizontalDifference(abstractLayouter) / (abstractLayouter.getRowSize() + 1)
        var offsetDifference = 0
        for (item in row) {
            val childRect = item.viewRect
            offsetDifference += difference
            childRect.left += offsetDifference
            childRect.right += offsetDifference
        }
    }
}