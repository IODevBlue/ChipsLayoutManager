package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter


abstract class AbstractFinishingCriteriaDecorator(private val finishingCriteria: IFinishingCriteria):
    IFinishingCriteria {

    override fun isFinishedLayouting(abstractLayouter: AbstractLayouter) = finishingCriteria.isFinishedLayouting(abstractLayouter)

}
