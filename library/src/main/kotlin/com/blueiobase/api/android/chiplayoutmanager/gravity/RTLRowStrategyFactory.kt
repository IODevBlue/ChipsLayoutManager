package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.main.ChipLayoutManager
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategyFactory

class RTLRowStrategyFactory: IRowStrategyFactory {
    override fun createRowStrategy(rowStrategy: Int): IRowStrategy {
        return when (rowStrategy) {
            ChipLayoutManager.STRATEGY_CENTER -> RTLRowFillSpaceCenterStrategy()
            ChipLayoutManager.STRATEGY_FILL_SPACE -> RTLRowFillSpaceStrategy()
            ChipLayoutManager.STRATEGY_FILL_VIEW -> RTLRowFillStrategy()
            ChipLayoutManager.STRATEGY_CENTER_DENSE -> RTLRowFillSpaceCenterDenseStrategy()
            ChipLayoutManager.STRATEGY_DEFAULT -> EmptyRowStrategy()
            else -> EmptyRowStrategy()
        }
    }
}