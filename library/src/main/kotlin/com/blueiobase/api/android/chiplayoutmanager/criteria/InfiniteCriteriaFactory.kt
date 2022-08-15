package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria


class InfiniteCriteriaFactory: AbstractCriteriaFactory() {

    override fun getBackwardFinishingCriteria(): IFinishingCriteria {
        return InfiniteCriteria()
    }

    override fun getForwardFinishingCriteria(): IFinishingCriteria {
        return InfiniteCriteria()
    }
}