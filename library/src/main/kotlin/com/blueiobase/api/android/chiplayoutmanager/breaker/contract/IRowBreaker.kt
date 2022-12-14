package com.blueiobase.api.android.chiplayoutmanager.breaker.contract

import android.view.View
import androidx.annotation.IntRange

/**
 * Interface to decide if a row should be broken from a specified [View] position.
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
interface IRowBreaker {

    /**
     * Validates if the given [position] represents the last [View] on the row.
     * @param position The position of the last [View] on the row.
     * @return `true` if it is the last [View] in the row. `false` if otherwise.
     *
     */
    fun isItemBreakRow(@IntRange(from = 0) position: Int): Boolean
}