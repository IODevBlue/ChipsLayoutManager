package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item


class SkipLastRowStrategy(rowStrategy: IRowStrategy, private val skipLastRow: Boolean): StrategyDecorator(rowStrategy) {

    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        //if !canNotBePlacedInCurrentRow and apply strategy called probably it is last row
        //so skip applying strategy
        if (skipLastRow && !abstractLayouter.isRowCompleted) return
        super.applyStrategy(abstractLayouter, row)
    }
}