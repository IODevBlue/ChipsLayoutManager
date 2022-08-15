package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter


class CriteriaRightAdditionalWidth(finishingCriteria: IFinishingCriteria, private val additionalWidth: Int):
    AbstractFinishingCriteriaDecorator(finishingCriteria) {

    override fun isFinishedLayouting(abstractLayouter: AbstractLayouter): Boolean {
        val rightBorder = abstractLayouter.getCanvasRightBorder()
        return super.isFinishedLayouting(abstractLayouter) &&  //if additional height filled
                abstractLayouter.viewLeft > rightBorder + additionalWidth
    }
}