package com.blueiobase.api.android.chiplayoutmanager.layouter

import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategyFactory
import com.blueiobase.api.android.chiplayoutmanager.gravity.RTLRowStrategyFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.RTLRowBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouterCreator
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IOrientationStateFactory


class RTLRowsOrientationStateFactory : IOrientationStateFactory {

    override fun createLayouterCreator(layoutManager: RecyclerView.LayoutManager): ILayouterCreator {
        return RTLRowsCreator(layoutManager)
    }

    override fun createRowStrategyFactory(): IRowStrategyFactory {
        return RTLRowStrategyFactory()
    }

    override fun createDefaultBreaker(): IBreakerFactory {
        return RTLRowBreakerFactory()
    }
}
