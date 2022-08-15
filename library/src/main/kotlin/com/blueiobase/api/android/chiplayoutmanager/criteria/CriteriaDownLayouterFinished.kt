package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter


class CriteriaDownLayouterFinished : IFinishingCriteria {
    private var isFinished = false

    override fun isFinishedLayouting(abstractLayouter: AbstractLayouter): Boolean {
        isFinished =
            isFinished || abstractLayouter.getViewTop() >= abstractLayouter.getCanvasBottomBorder()
        return isFinished
    }
}