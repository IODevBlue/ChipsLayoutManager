package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item

class EmptyRowStrategy: IRowStrategy {

    override fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>) {
        //do nothing
    }
}