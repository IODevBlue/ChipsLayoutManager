package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter

class CriteriaUpAdditionalHeight(finishingCriteria: IFinishingCriteria, private val additionalHeight: Int) :
    AbstractFinishingCriteriaDecorator(finishingCriteria) {

    override fun isFinishedLayouting(abstractLayouter: AbstractLayouter): Boolean {
        val topBorder = abstractLayouter.getCanvasTopBorder()
        return super.isFinishedLayouting(abstractLayouter) &&  //if additional height filled
                abstractLayouter.getViewBottom() < topBorder - additionalHeight
    }
}