package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.main.ChipLayoutManager
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IScrollingController
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.anchor.ColumnsAnchorFactory
import com.blueiobase.api.android.chiplayoutmanager.anchor.contract.IAnchorFactory
import com.blueiobase.api.android.chiplayoutmanager.cache.contract.IViewCacheStorage
import com.blueiobase.api.android.chiplayoutmanager.gravity.ColumnGravityModifiersFactory
import com.blueiobase.api.android.chiplayoutmanager.gravity.ColumnStrategyFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.ColumnBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.DecoratorBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.AbstractCriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.ColumnsCriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.ICriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.InfiniteCriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ICanvas
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IStateFactory
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacerFactory
import com.blueiobase.api.android.chiplayoutmanager.util.StateHelper


class ColumnsStateFactory(private val layoutManager: ChipLayoutManager) : IStateFactory {

    private val rowStrategyFactory = ColumnStrategyFactory()

    override fun createLayouterFactory(criteriaFactory: ICriteriaFactory, placerFactory: IPlacerFactory): LayouterFactory {
        val cacheStorage: IViewCacheStorage = layoutManager.viewPositionsStorage
        return createColumnLayouterFactory(criteriaFactory, placerFactory, cacheStorage)
    }

    private fun createColumnLayouterFactory(
        criteriaFactory: ICriteriaFactory,
        placerFactory: IPlacerFactory,
        cacheStorage: IViewCacheStorage
    ): LayouterFactory {
        return LayouterFactory(
            layoutManager,
            ColumnsCreator(layoutManager),
            DecoratorBreakerFactory(
                cacheStorage,
                layoutManager.getRowBreaker(),
                layoutManager.getMaxViewsInRow(),
                ColumnBreakerFactory()
            ),
            criteriaFactory,
            placerFactory,
            ColumnGravityModifiersFactory(),
            rowStrategyFactory.createRowStrategy(layoutManager.getRowStrategyType())
        )
    }

    override fun createDefaultFinishingCriteriaFactory(): AbstractCriteriaFactory {
        return if (StateHelper.isInfinite(this)) InfiniteCriteriaFactory() else ColumnsCriteriaFactory()
    }

    override fun anchorFactory(): IAnchorFactory {
        return ColumnsAnchorFactory(layoutManager, layoutManager.canvas!!)
    }

    override fun scrollingController(): IScrollingController {
        return layoutManager.horizontalScrollingController()
    }

    override fun createCanvas(): ICanvas {
        return ColumnSquare(layoutManager)
    }

    override fun getSizeMode(): Int {
        return layoutManager.widthMode
    }

    override fun getStart(): Int {
        return 0
    }

    override fun getStart(view: View): Int {
        return layoutManager.getDecoratedLeft(view)
    }

    override fun getStart(anchor: AnchorViewState): Int {
        return anchor.anchorViewRect!!.left
    }

    override fun getEnd(): Int {
        return layoutManager.width
    }

    override fun getEnd(view: View): Int {
        return layoutManager.getDecoratedRight(view)
    }

    override fun getEnd(anchor: AnchorViewState): Int {
        return anchor.anchorViewRect!!.right
    }

    override fun getEndViewPosition() = layoutManager.getPosition(layoutManager.canvas?.getBottomView()!!)

    override fun getStartAfterPadding() = layoutManager.paddingLeft

    override fun getStartViewPosition() = layoutManager.getPosition(layoutManager.canvas?.getTopView()!!)

    override fun getEndAfterPadding() = layoutManager.width - layoutManager.paddingRight

    override fun getStartViewBound() = getStart(layoutManager.canvas?.getLeftView()!!)

    override fun getEndViewBound() = getEnd(layoutManager.canvas?.getRightView()!!)

    override fun getTotalSpace() = (layoutManager.width - layoutManager.paddingLeft - layoutManager.paddingRight)

}