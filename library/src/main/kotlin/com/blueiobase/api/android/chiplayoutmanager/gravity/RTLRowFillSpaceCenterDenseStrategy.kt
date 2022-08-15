package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.util.GravityUtil.getHorizontalDifference
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item

class RTLRowFillSpaceCenterDenseStrategy: IRowStrategy {
    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        val difference = getHorizontalDifference(abstractLayouter) / 2

        for (item in row) {
            val childRect = item.viewRect
            childRect.left -= difference
            childRect.right -= difference
        }
    }
}