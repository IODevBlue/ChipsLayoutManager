package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter


class CriteriaRightLayouterFinished : IFinishingCriteria {

    override fun isFinishedLayouting(abstractLayouter: AbstractLayouter) =
        abstractLayouter.viewLeft >= abstractLayouter.getCanvasRightBorder()

}