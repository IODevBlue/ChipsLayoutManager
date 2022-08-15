package com.blueiobase.api.android.chiplayoutmanager.gravity.contract

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.model.Item


interface IRowStrategy {
    fun applyStrategy(abstractLayouter: AbstractLayouter, row: ArrayList<Item>)
}