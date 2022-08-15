package com.blueiobase.api.android.chiplayoutmanager.layouter.contract

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IScrollingController
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.anchor.contract.IAnchorFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.AbstractCriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.criteria.contract.ICriteriaFactory
import com.blueiobase.api.android.chiplayoutmanager.layouter.LayouterFactory
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacerFactory


interface IStateFactory {

    fun createLayouterFactory(criteriaFactory: ICriteriaFactory, placerFactory: IPlacerFactory): LayouterFactory

    fun createDefaultFinishingCriteriaFactory(): AbstractCriteriaFactory

    fun anchorFactory(): IAnchorFactory

    fun scrollingController(): IScrollingController

    fun createCanvas(): ICanvas

    fun getSizeMode(): Int

    fun getStart(): Int

    fun getStart(view: View): Int

    fun getStart(anchor: AnchorViewState): Int

    fun getStartAfterPadding(): Int

    fun getStartViewPosition(): Int

    fun getStartViewBound(): Int

    fun getEnd(): Int

    fun getEnd(view: View): Int

    fun getEndAfterPadding(): Int

    fun getEnd(anchor: AnchorViewState): Int

    fun getEndViewPosition(): Int

    fun getEndViewBound(): Int

    fun getTotalSpace(): Int
}