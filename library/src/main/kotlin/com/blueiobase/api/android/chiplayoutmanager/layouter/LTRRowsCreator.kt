package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouterCreator


class LTRRowsCreator(private val layoutManager: RecyclerView.LayoutManager) :
    ILayouterCreator {

    override fun createOffsetRectForBackwardLayouter(anchorRect: AnchorViewState): Rect {
        val anchorRect1 = anchorRect.anchorViewRect
        return Rect(
            0,
            anchorRect1?.top ?: 0,  //we shouldn't include anchor view here, so anchorLeft is a rightOffset
            anchorRect1?.left ?: 0,
            anchorRect1?.bottom ?: 0
        )
    }

    override fun createOffsetRectForForwardLayouter(anchorRect: AnchorViewState): Rect {
        val anchorRect1 = anchorRect.anchorViewRect
        return Rect( //we should include anchor view here, so anchorLeft is a leftOffset
            anchorRect1?.left ?: layoutManager.paddingLeft,
            anchorRect1?.top ?: if (anchorRect.position == 0) layoutManager.paddingTop else 0,
            0,
            anchorRect1?.bottom ?: if (anchorRect.position == 0) layoutManager.paddingBottom else 0
        )
    }

    override fun createBackwardBuilder(): AbstractLayouter.Companion.Builder {
        return LTRUpLayouter.newBuilder()
    }

    override fun createForwardBuilder(): AbstractLayouter.Companion.Builder {
        return LTRDownLayouter.newBuilder()
    }
}
