package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter


/** when using this criteria [AbstractLayouter] doesn't able to finish himself, you should only stop calling placeView outside  */
class InfiniteCriteria : IFinishingCriteria {
    override fun isFinishedLayouting(abstractLayouter: AbstractLayouter) = false

}