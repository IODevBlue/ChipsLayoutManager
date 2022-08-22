package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategyFactory

class RTLRowStrategyFactory: IRowStrategyFactory {
    override fun createRowStrategy(rowStrategy: Int): IRowStrategy {
        return when (rowStrategy) {
            ChipsLayoutManager.STRATEGY_CENTER -> RTLRowFillSpaceCenterStrategy()
            ChipsLayoutManager.STRATEGY_FILL_SPACE -> RTLRowFillSpaceStrategy()
            ChipsLayoutManager.STRATEGY_FILL_VIEW -> RTLRowFillStrategy()
            ChipsLayoutManager.STRATEGY_CENTER_DENSE -> RTLRowFillSpaceCenterDenseStrategy()
            ChipsLayoutManager.STRATEGY_DEFAULT -> EmptyRowStrategy()
            else -> EmptyRowStrategy()
        }
    }
}