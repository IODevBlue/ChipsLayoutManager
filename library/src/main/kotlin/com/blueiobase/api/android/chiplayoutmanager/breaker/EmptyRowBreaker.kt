package com.blueiobase.api.android.chiplayoutmanager.breaker

import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IRowBreaker

/**
 * An [IRowBreaker] implementation which does not perform any row breaks/splice operations.
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
class EmptyRowBreaker: IRowBreaker {
    override fun isItemBreakRow(position: Int) = false
}