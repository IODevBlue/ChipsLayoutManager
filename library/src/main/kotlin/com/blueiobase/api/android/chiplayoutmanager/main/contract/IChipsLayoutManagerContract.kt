package com.blueiobase.api.android.chiplayoutmanager.main.contract

import androidx.annotation.IntRange
import com.blueiobase.api.android.chiplayoutmanager.annotation.Orientation
import com.blueiobase.api.android.chiplayoutmanager.annotation.RowStrategy
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IRowBreaker

interface IChipsLayoutManagerContract: IPositionsContract, IScrollingContract {

    /** use it to strictly disable scrolling.
     * If scrolling enabled it would be disabled in case all items fit on the screen  */
    override fun setScrollingEnabledContract(isEnabled: Boolean)

    /**
     * change max count of row views in runtime
     */
    fun setMaxViewsInRow(@IntRange(from = 1) maxViewsInRow: Int)

    /** retrieve max views in row settings */
    fun getMaxViewsInRow(): Int

    /** retrieve instantiated row breaker */
    fun getRowBreaker(): IRowBreaker?

    /** retrieve row strategy type */
    @RowStrategy
    fun getRowStrategyType(): Int

    /** orientation type of layout manager */
    @Orientation
    fun layoutOrientation(): Int

    /** whether or not scrolling disabled outside */
    override fun isScrollingEnabledContract(): Boolean
}