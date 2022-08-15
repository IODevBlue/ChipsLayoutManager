package com.blueiobase.api.android.chiplayoutmanager.gravity.contract

import com.blueiobase.api.android.chiplayoutmanager.annotation.RowStrategy

interface IRowStrategyFactory {
    fun createRowStrategy(@RowStrategy rowStrategy: Int): IRowStrategy
}