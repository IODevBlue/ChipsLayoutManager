package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IRowBreaker

class EmptyRowBreaker: IRowBreaker {
    override fun isItemBreakRow(position: Int) = false
}