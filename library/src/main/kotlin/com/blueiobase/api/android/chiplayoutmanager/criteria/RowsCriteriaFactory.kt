package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.ICriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria


class RowsCriteriaFactory : AbstractCriteriaFactory(), ICriteriaFactory {

    override fun getBackwardFinishingCriteria(): IFinishingCriteria {
        var criteria: IFinishingCriteria = CriteriaUpLayouterFinished()
        if (additionalLength != 0) {
            criteria = CriteriaUpAdditionalHeight(criteria, additionalLength)
        }
        return criteria
    }

    override fun getForwardFinishingCriteria(): IFinishingCriteria {
        var criteria: IFinishingCriteria = CriteriaDownLayouterFinished()
        if (additionalLength != 0) {
            criteria = CriteriaDownAdditionalHeight(criteria, additionalLength)
        }
        if (additionalRowCount != 0) {
            criteria = CriteriaAdditionalRow(criteria, additionalRowCount)
        }
        return criteria
    }
}