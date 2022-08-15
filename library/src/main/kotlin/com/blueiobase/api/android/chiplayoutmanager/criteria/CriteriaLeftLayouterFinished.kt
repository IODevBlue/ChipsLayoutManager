package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter


class CriteriaLeftLayouterFinished : IFinishingCriteria {

    override fun isFinishedLayouting(abstractLayouter: AbstractLayouter): Boolean {
        return abstractLayouter.viewRight <= abstractLayouter.getCanvasLeftBorder()
    }
}