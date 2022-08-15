package com.blueiobase.api.android.chiplayoutmanager.gravity

import android.graphics.Rect
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item
import com.blueiobase.api.android.chiplayoutmanager.util.GravityUtil


class ColumnFillSpaceCenterDenseStrategy: IRowStrategy {

    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        val difference: Int = GravityUtil.getVerticalDifference(abstractLayouter) / 2
        for (item in row) {
            val childRect: Rect = item.viewRect
            childRect.top += difference
            childRect.bottom += difference
        }
    }
}