package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter

class EmtpyCriteria : IFinishingCriteria {
    override fun isFinishedLayouting(abstractLayouter: AbstractLayouter) = true

}