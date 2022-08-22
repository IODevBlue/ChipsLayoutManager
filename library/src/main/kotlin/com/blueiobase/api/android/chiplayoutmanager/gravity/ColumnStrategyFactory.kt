package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager

import com.blueiobase.api.android.chiplayoutmanager.annotation.RowStrategy
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategyFactory

class ColumnStrategyFactory: IRowStrategyFactory {

    override fun createRowStrategy(@RowStrategy rowStrategy: Int): IRowStrategy {
        return when (rowStrategy) {
            ChipsLayoutManager.STRATEGY_CENTER -> ColumnFillSpaceCenterStrategy()
            ChipsLayoutManager.STRATEGY_CENTER_DENSE -> ColumnFillSpaceCenterDenseStrategy()
            ChipsLayoutManager.STRATEGY_FILL_SPACE -> ColumnFillSpaceStrategy()
            ChipsLayoutManager.STRATEGY_FILL_VIEW -> ColumnFillStrategy()
            ChipsLayoutManager.STRATEGY_DEFAULT -> EmptyRowStrategy()
            else -> EmptyRowStrategy()
        }
    }
}