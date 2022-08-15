package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.main.ChipLayoutManager
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategyFactory


class LTRRowStrategyFactory: IRowStrategyFactory {
    override fun createRowStrategy(rowStrategy: Int): IRowStrategy {
        return when (rowStrategy) {
            ChipLayoutManager.STRATEGY_CENTER -> LTRRowFillSpaceCenterStrategy()
            ChipLayoutManager.STRATEGY_CENTER_DENSE -> LTRRowFillSpaceCenterDenseStrategy()
            ChipLayoutManager.STRATEGY_FILL_SPACE -> LTRRowFillSpaceStrategy()
            ChipLayoutManager.STRATEGY_FILL_VIEW -> LTRRowFillStrategy()
            ChipLayoutManager.STRATEGY_DEFAULT -> EmptyRowStrategy()
            else -> EmptyRowStrategy()
        }
    }
}