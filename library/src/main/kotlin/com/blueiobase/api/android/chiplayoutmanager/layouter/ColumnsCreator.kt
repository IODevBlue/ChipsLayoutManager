package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ILayouterCreator

class ColumnsCreator(private val layoutManager: RecyclerView.LayoutManager): ILayouterCreator {

    override fun createBackwardBuilder() = LeftLayouter.newBuilder()

    override fun createForwardBuilder() = RightLayouter.newBuilder()

    override fun createOffsetRectForBackwardLayouter(anchorRect: AnchorViewState): Rect {
        val anchorViewRect = anchorRect.anchorViewRect
        return Rect(
            anchorViewRect?.left ?: 0,
            0,
            anchorViewRect?.right ?: 0,  //we shouldn't include anchor view here, so anchorTop is a bottomOffset
            anchorViewRect?.top ?: 0
        )
    }

    override fun createOffsetRectForForwardLayouter(anchorRect: AnchorViewState): Rect {
        val anchorViewRect = anchorRect.anchorViewRect
        return Rect(
            anchorViewRect?.left ?: if (anchorRect.position == 0) layoutManager.paddingLeft else 0,  //we should include anchor view here, so anchorTop is a topOffset
            anchorViewRect?.top ?: layoutManager.paddingTop,
            anchorViewRect?.right ?: if (anchorRect.position == 0) layoutManager.paddingRight else 0,
            0
        )
    }
}
