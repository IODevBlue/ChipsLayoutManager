package com.blueiobase.api.android.chiplayoutmanager.breaker.contract

import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter

interface ILayoutRowBreaker {
    fun isRowBroke(al: AbstractLayouter): Boolean
}