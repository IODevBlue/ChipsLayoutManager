package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.main.ChipLayoutManager
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IScrollingController
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.anchor.contract.IAnchorFactory
import com.blueiobase.api.android.chiplayoutmanager.anchor.RowsAnchorFactory
import com.blueiobase.api.android.chiplayoutmanager.gravity.RowGravityModifiersFactory
import com.blueiobase.api.android.chiplayoutmanager.breaker.DecoratorBreakerFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.AbstractCriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.ICriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.InfiniteCriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.RowsCriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ICanvas
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IOrientationStateFactory
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IStateFactory
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacerFactory
import com.blueiobase.api.android.chiplayoutmanager.util.StateHelper

class RowsStateFactory(private val layoutManager: ChipLayoutManager) : IStateFactory {

    private fun createOrientationStateFactory(): IOrientationStateFactory {
        return if (layoutManager.isLayoutRTL()) RTLRowsOrientationStateFactory() else LTRRowsOrientationStateFactory()
    }

    override fun createLayouterFactory(criteriaFactory: ICriteriaFactory, placerFactory: IPlacerFactory): LayouterFactory {
        val orientationStateFactory: IOrientationStateFactory = createOrientationStateFactory()
        return LayouterFactory(
            layoutManager,
            orientationStateFactory.createLayouterCreator(layoutManager),
            DecoratorBreakerFactory(
                layoutManager.viewPositionsStorage,
                layoutManager.getRowBreaker(),
                layoutManager.getMaxViewsInRow(),
                orientationStateFactory.createDefaultBreaker()
            ),
            criteriaFactory,
            placerFactory,
            RowGravityModifiersFactory(),
            orientationStateFactory.createRowStrategyFactory()
                .createRowStrategy(layoutManager.getRowStrategyType())
        )
    }

    override fun createDefaultFinishingCriteriaFactory(): AbstractCriteriaFactory {
        return if (StateHelper.isInfinite(this)) InfiniteCriteriaFactory() else RowsCriteriaFactory()
    }

    override fun anchorFactory(): IAnchorFactory {
        return RowsAnchorFactory(layoutManager, layoutManager.canvas!!)
    }

    override fun scrollingController(): IScrollingController {
        return layoutManager.verticalScrollingController()
    }

    override fun createCanvas(): ICanvas {
        return RowSquare(layoutManager)
    }

    override fun getSizeMode(): Int {
        return layoutManager.heightMode
    }

    override fun getStart(): Int {
        return 0
    }

    override fun getStart(view: View): Int {
        return layoutManager.getDecoratedTop(view)
    }

    override fun getStart(anchor: AnchorViewState): Int {
        return anchor.anchorViewRect!!.top
    }

    override fun getEnd(): Int {
        return layoutManager.height
    }

    override fun getEnd(view: View): Int {
        return layoutManager.getDecoratedBottom(view)
    }

    override fun getEnd(anchor: AnchorViewState): Int {
        return anchor.anchorViewRect!!.bottom
    }

    override fun getEndViewPosition(): Int {
        return layoutManager.getPosition(layoutManager.canvas!!.getRightView()!!)
    }

    override fun getStartAfterPadding(): Int {
        return layoutManager.paddingTop
    }

    override fun getStartViewPosition(): Int {
        return layoutManager.getPosition(layoutManager.canvas!!.getLeftView()!!)
    }

    override fun getEndAfterPadding(): Int {
        return layoutManager.height - layoutManager.paddingBottom
    }

    override fun getStartViewBound(): Int {
        return getStart(layoutManager.canvas!!.getTopView()!!)
    }

    override fun getEndViewBound(): Int {
        return getEnd(layoutManager.canvas!!.getBottomView()!!)
    }

    override fun getTotalSpace(): Int {
        return (layoutManager.height - layoutManager.paddingTop
                - layoutManager.paddingBottom)
    }
}