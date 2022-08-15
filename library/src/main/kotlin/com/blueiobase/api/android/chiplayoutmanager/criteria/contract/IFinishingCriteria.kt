package com.blueiobase.api.android.chiplayoutmanager.criteria.contract

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter

interface IFinishingCriteria {
    /** check if layouting finished by criteria  */
    fun isFinishedLayouting(abstractLayouter: AbstractLayouter): Boolean

}