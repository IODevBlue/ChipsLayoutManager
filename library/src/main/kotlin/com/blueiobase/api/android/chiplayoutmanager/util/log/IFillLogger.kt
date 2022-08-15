package com.blueiobase.api.android.chiplayoutmanager.util.log

import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState

interface IFillLogger {

    fun onStartLayouter(startPosition: Int)

    fun onItemRequested()

    fun onItemRecycled()

    fun onFinishedLayouter()

    fun onAfterLayouter()

    fun onRemovedAndRecycled(position: Int)

    fun onAfterRemovingViews()

    fun onBeforeLayouter(state: AnchorViewState?)
}