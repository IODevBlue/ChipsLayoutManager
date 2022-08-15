package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouterListener


class CriteriaPolitePositionReached constructor(abstractLayouter: AbstractLayouter,
                                                finishingCriteria: IFinishingCriteria,
                                                private val reachedPosition: Int) :
    AbstractFinishingCriteriaDecorator(finishingCriteria), IFinishingCriteria, ILayouterListener {

    private var isPositionReached = false
    override fun isFinishedLayouting(abstractLayouter: AbstractLayouter): Boolean {
        val isFinishedFlow = super.isFinishedLayouting(abstractLayouter)
        return isFinishedFlow || isPositionReached
    }

    override fun onLayoutRow(layouter: ILayouter) {
        if (isPositionReached) return
        if (layouter.getRowSize() == 0) return
        for (item in layouter.getCurrentRowItems()) {
            if (item.viewPosition == reachedPosition) {
                isPositionReached = true
                return
            }
        }
    }

    init {
        abstractLayouter.addLayouterListener(this)
    }
}
