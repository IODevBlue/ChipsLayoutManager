package com.blueiobase.api.android.chiplayoutmanager.breaker.contract

import androidx.annotation.IntRange

/** determines whether LM should break row from view position  */
interface IRowBreaker {
    /** @return `true` means that it is the last view in the row.
     * `false` means that breaking behaviour about current view will be based on another conditions
     */
    fun isItemBreakRow(@IntRange(from = 0) position: Int): Boolean
}