package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategyFactory


class LTRRowStrategyFactory: IRowStrategyFactory {
    override fun createRowStrategy(rowStrategy: Int): IRowStrategy {
        return when (rowStrategy) {
            ChipsLayoutManager.STRATEGY_CENTER -> LTRRowFillSpaceCenterStrategy()
            ChipsLayoutManager.STRATEGY_CENTER_DENSE -> LTRRowFillSpaceCenterDenseStrategy()
            ChipsLayoutManager.STRATEGY_FILL_SPACE -> LTRRowFillSpaceStrategy()
            ChipsLayoutManager.STRATEGY_FILL_VIEW -> LTRRowFillStrategy()
            ChipsLayoutManager.STRATEGY_DEFAULT -> EmptyRowStrategy()
            else -> EmptyRowStrategy()
        }
    }
}