package com.blueiobase.api.android.chiplayoutmanager.layouter.contract

import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategyFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory

interface IOrientationStateFactory {
    fun createLayouterCreator(layoutManager: RecyclerView.LayoutManager): ILayouterCreator
    fun createRowStrategyFactory(): IRowStrategyFactory
    fun createDefaultBreaker(): IBreakerFactory
}