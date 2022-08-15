package com.blueiobase.api.android.chiplayoutmanager.criteria

import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.ICriteriaFactory

abstract class AbstractCriteriaFactory: ICriteriaFactory {

    var additionalLength = 0
        set(value) {
            //if (value < 0) throw IllegalArgumentException("additional height can't be negative")
            require(value > 0) {"Additional height can't be negative"}
           field = value
        }

    var additionalRowCount = 0

}