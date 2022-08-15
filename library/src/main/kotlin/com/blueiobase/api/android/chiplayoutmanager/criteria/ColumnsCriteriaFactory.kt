package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria


class ColumnsCriteriaFactory: AbstractCriteriaFactory() {

    override fun getBackwardFinishingCriteria(): IFinishingCriteria {
        var criteria: IFinishingCriteria = CriteriaLeftLayouterFinished()
        if (additionalLength != 0) {
            criteria = CriteriaLeftAdditionalWidth(criteria, additionalLength)
        }
        return criteria
    }

    override fun getForwardFinishingCriteria(): IFinishingCriteria {
        var criteria: IFinishingCriteria = CriteriaRightLayouterFinished()
        if (additionalLength != 0) {
            criteria = CriteriaRightAdditionalWidth(criteria, additionalLength)
        }
        if (additionalRowCount != 0) {
            criteria = CriteriaAdditionalRow(criteria, additionalRowCount)
        }
        return criteria
    }
}