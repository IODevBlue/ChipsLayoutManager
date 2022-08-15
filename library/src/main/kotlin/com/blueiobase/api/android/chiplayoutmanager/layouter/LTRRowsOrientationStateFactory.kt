package com.blueiobase.api.android.chiplayoutmanager.layouter

import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategyFactory
import com.blueiobase.api.android.chiplayoutmanager.gravity.LTRRowStrategyFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.LTRRowBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouterCreator
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IOrientationStateFactory


class LTRRowsOrientationStateFactory : IOrientationStateFactory {
    override fun createLayouterCreator(layoutManager: RecyclerView.LayoutManager): ILayouterCreator {
        return LTRRowsCreator(layoutManager)
    }

    override fun createRowStrategyFactory(): IRowStrategyFactory {
        return LTRRowStrategyFactory()
    }

    override fun createDefaultBreaker(): IBreakerFactory {
        return LTRRowBreakerFactory()
    }
}
