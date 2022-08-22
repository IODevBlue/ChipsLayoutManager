package com.blueiobase.api.android.chiplayoutmanager.layouter

import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.cache.contract.IViewCacheStorage
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifiersFactory
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.gravity.SkipLastRowStrategy
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.IBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.ICriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouter
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouterCreator
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouterListener
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacerFactory


class LayouterFactory constructor(
    private val layoutManager: ChipsLayoutManager,
    private val layouterCreator: ILayouterCreator,
    private val breakerFactory: IBreakerFactory,
    private val criteriaFactory: ICriteriaFactory,
    private val placerFactory: IPlacerFactory,
    private val gravityModifiersFactory: IGravityModifiersFactory,
    private val rowStrategy: IRowStrategy
) {

    private val cacheStorage: IViewCacheStorage
    private val layouterListeners = ArrayList<ILayouterListener>()

    init {
        cacheStorage = layoutManager.viewPositionsStorage
    }

    fun addLayouterListener(layouterListener: ILayouterListener?) {
        layouterListener?.let {
            layouterListeners.add(it)
        }
    }

    private fun createBackwardBuilder(): AbstractLayouter.Companion.Builder =  layouterCreator.createBackwardBuilder()

    private fun createForwardBuilder(): AbstractLayouter.Companion.Builder = layouterCreator.createForwardBuilder()

    private fun createOffsetRectForBackwardLayouter(anchorRect: AnchorViewState) = layouterCreator.createOffsetRectForBackwardLayouter(anchorRect)

    private fun createOffsetRectForForwardLayouter(anchorRect: AnchorViewState) = layouterCreator.createOffsetRectForForwardLayouter(anchorRect)

    private fun createCanvas() = layoutManager.canvas

    private fun fillBasicBuilder(builder: AbstractLayouter.Companion.Builder)  =
        builder.layoutManager(layoutManager)
            .canvas(createCanvas())
            .childGravityResolver(layoutManager.childGravityResolver)
            .cacheStorage(cacheStorage)
            .gravityModifiersFactory(gravityModifiersFactory)
            .addLayouterListeners(layouterListeners)

    fun getBackwardLayouter(anchorRect: AnchorViewState): ILayouter {
        return fillBasicBuilder(createBackwardBuilder())
            .offsetRect(createOffsetRectForBackwardLayouter(anchorRect))
            .breaker(breakerFactory.createBackwardRowBreaker())
            .finishingCriteria(criteriaFactory.getBackwardFinishingCriteria())
            .rowStrategy(rowStrategy)
            .placer(placerFactory.getAtStartPlacer())
            .positionIterator(DecrementalPositionIterator(layoutManager.itemCount))
            .build()
    }

    fun getForwardLayouter(anchorRect: AnchorViewState): ILayouter {
        return fillBasicBuilder(createForwardBuilder())
            .offsetRect(createOffsetRectForForwardLayouter(anchorRect))
            .breaker(breakerFactory.createForwardRowBreaker())
            .finishingCriteria(criteriaFactory.getForwardFinishingCriteria())
            .rowStrategy(
                SkipLastRowStrategy(
                    rowStrategy,
                    !layoutManager.isStrategyAppliedWithLastRow
                )
            )
            .placer(placerFactory.getAtEndPlacer())
            .positionIterator(IncrementalPositionIterator(layoutManager.itemCount))
            .build()
    }

    fun buildForwardLayouter(layouter: ILayouter): ILayouter {
        val abstractLayouter = layouter as AbstractLayouter
        abstractLayouter.finishingCriteria = criteriaFactory.getForwardFinishingCriteria()
        abstractLayouter.placer = placerFactory.getAtEndPlacer()
        return abstractLayouter
    }

    fun buildBackwardLayouter(layouter: ILayouter): ILayouter {
        val abstractLayouter = layouter as AbstractLayouter
        abstractLayouter.finishingCriteria = criteriaFactory.getBackwardFinishingCriteria()
        abstractLayouter.placer = placerFactory.getAtStartPlacer()
        return abstractLayouter
    }


}
