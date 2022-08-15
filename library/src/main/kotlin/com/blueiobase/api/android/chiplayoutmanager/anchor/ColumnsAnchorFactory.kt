package com.blueiobase.api.android.chiplayoutmanager.anchor

import android.view.View
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.main.ChildViewsIterable
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ICanvas
import com.blueiobase.api.android.chiplayoutmanager.main.ChipLayoutManager

/**
 * This class is responsible for handling the [View] object anchored at the extreme left/start of the [layout manager][RecyclerView.LayoutManager]
 * when the layout orientation is [vertical][ChipLayoutManager.VERTICAL].
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
class ColumnsAnchorFactory(private val layoutManager: RecyclerView.LayoutManager, canvas: ICanvas): AbstractAnchorFactory(layoutManager, canvas) {

    /**
     * The [View] objects that the current [layout manager][RecyclerView.LayoutManager] is responsible for.
     */
    private val childViews = ChildViewsIterable(layoutManager)

    override fun getAnchor(): AnchorViewState {
        var minPosView = AnchorViewState.getNotFoundState()
        var minPosition = Int.MAX_VALUE
        var minLeft = Int.MAX_VALUE
        var maxRight = Int.MIN_VALUE
        for (view in childViews) {
            val anchorViewState = createAnchorState(view)
            layoutManager.apply {
                val pos = getPosition(view)
                val left = getDecoratedLeft(view)
                val right = getDecoratedRight(view)
                val viewRect = Rect(anchorViewState.anchorViewRect)
                if (canvas.isInside(viewRect) && !anchorViewState.isRemoving()) {
                    if (minPosition > pos) {
                        minPosition = pos
                        minPosView = anchorViewState
                    }
                    if (minLeft > left) {
                        minLeft = left
                        maxRight = right
                    } else if (minLeft == left) {
                        maxRight = maxRight.coerceAtLeast(right)
                    }
                }
            }

        }
        if (!minPosView.isNotFoundState()) {
            minPosView.anchorViewRect?.apply {
                left = minLeft
                right = maxRight
            }
            minPosView.position = minPosition
        }
        return minPosView
    }

    override fun resetRowCoordinates(anchorViewState: AnchorViewState) {
        if (!anchorViewState.isNotFoundState()) {
            val rect = anchorViewState.anchorViewRect
            rect?.apply {
                top = canvas.getCanvasTopBorder()
                bottom = canvas.getCanvasBottomBorder()
            }
        }
    }
}