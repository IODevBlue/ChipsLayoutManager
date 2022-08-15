package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouterCreator

class RTLRowsCreator(private val layoutManager: RecyclerView.LayoutManager) : ILayouterCreator {
    //---- up layouter below

    override fun createOffsetRectForBackwardLayouter(anchorRect: AnchorViewState): Rect {
        val anchorRect1 = anchorRect.anchorViewRect
        return Rect( //we shouldn't include anchor view here, so anchorLeft is a rightOffset
            anchorRect1?.right ?: 0,
            anchorRect1?.top ?: 0,
            0,
            anchorRect1?.bottom ?: 0
        )
    }

    override fun createBackwardBuilder(): AbstractLayouter.Companion.Builder {
        return RTLUpLayouter.newBuilder()
    }

    //---- down layouter below
    override fun createForwardBuilder(): AbstractLayouter.Companion.Builder {
        return RTLDownLayouter.newBuilder()
    }

    override fun createOffsetRectForForwardLayouter(anchorRect: AnchorViewState): Rect {
        val anchorRect1: Rect? = anchorRect.anchorViewRect
        return Rect(
            0,
            anchorRect1?.top ?: if (anchorRect.position == 0) layoutManager.paddingTop else 0,
            anchorRect1?.right ?: layoutManager.paddingRight,
            anchorRect1?.bottom ?: if (anchorRect.position == 0) layoutManager.paddingBottom else 0
        )
    }
}