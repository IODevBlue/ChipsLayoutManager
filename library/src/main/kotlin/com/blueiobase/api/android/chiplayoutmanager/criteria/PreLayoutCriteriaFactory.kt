package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.ICriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.IFinishingCriteria


class PreLayoutCriteriaFactory(private val additionalHeight: Int, private val additionalRowsCount: Int) :
    ICriteriaFactory {

    override fun getBackwardFinishingCriteria(): IFinishingCriteria {
        return CriteriaUpAdditionalHeight(CriteriaUpLayouterFinished(), additionalHeight)
    }

    override fun getForwardFinishingCriteria(): IFinishingCriteria {
        return CriteriaAdditionalRow(CriteriaDownAdditionalHeight(CriteriaDownLayouterFinished(), additionalHeight), additionalRowsCount)
    }
}