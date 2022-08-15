package com.blueiobase.api.android.chiplayoutmanager.layouter.contract

import android.graphics.Rect
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter

interface ILayouterCreator {
    //---- up layouter below
    fun createOffsetRectForBackwardLayouter(anchorRect: AnchorViewState): Rect
    fun createBackwardBuilder(): AbstractLayouter.Companion.Builder
    fun createForwardBuilder(): AbstractLayouter.Companion.Builder
    fun createOffsetRectForForwardLayouter(anchorRect: AnchorViewState): Rect
}
