package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.main.ChipLayoutManager

import com.blueiobase.api.android.chiplayoutmanager.annotation.RowStrategy
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategyFactory

class ColumnStrategyFactory: IRowStrategyFactory {

    override fun createRowStrategy(@RowStrategy rowStrategy: Int): IRowStrategy {
        return when (rowStrategy) {
            ChipLayoutManager.STRATEGY_CENTER -> ColumnFillSpaceCenterStrategy()
            ChipLayoutManager.STRATEGY_CENTER_DENSE -> ColumnFillSpaceCenterDenseStrategy()
            ChipLayoutManager.STRATEGY_FILL_SPACE -> ColumnFillSpaceStrategy()
            ChipLayoutManager.STRATEGY_FILL_VIEW -> ColumnFillStrategy()
            ChipLayoutManager.STRATEGY_DEFAULT -> EmptyRowStrategy()
            else -> EmptyRowStrategy()
        }
    }
}