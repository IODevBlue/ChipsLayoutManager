package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter

import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouter

import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouterListener

class CriteriaAdditionalRow(finishingCriteria: IFinishingCriteria, private val requiredRowsCount: Int): AbstractFinishingCriteriaDecorator(
    finishingCriteria
), IFinishingCriteria, ILayouterListener {
    private var additionalRowsCount = 0

    override fun isFinishedLayouting(abstractLayouter: AbstractLayouter): Boolean {
        abstractLayouter.addLayouterListener(this)
        return super.isFinishedLayouting(abstractLayouter) && additionalRowsCount >= requiredRowsCount
    }

    override fun onLayoutRow(layouter: ILayouter) {
        if (super.isFinishedLayouting((layouter as AbstractLayouter))) {
            additionalRowsCount++
        }
    }
}