package com.blueiobase.api.android.chiplayoutmanager.criteria.contract


interface ICriteriaFactory {

    fun getBackwardFinishingCriteria(): IFinishingCriteria

    fun getForwardFinishingCriteria(): IFinishingCriteria
}