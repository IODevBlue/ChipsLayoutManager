package com.blueiobase.api.android.chiplayoutmanager.main

import android.content.Context
import android.graphics.PointF
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.main.contract.IScrollingController
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IStateFactory

open class VerticalScrollingController(private val layoutManager: ChipsLayoutManager, stateFactory: IStateFactory, scrollerListener: IScrollerListener):
    ScrollingController(layoutManager, stateFactory, scrollerListener), IScrollingController {

    override fun createSmoothScroller(context: Context, position: Int, timeMs: Int, anchor: AnchorViewState): SmoothScroller {
        return object : LinearSmoothScroller(context) {
            /*
             * LinearSmoothScroller, at a minimum, just need to know the vector
             * (x/y distance) to travel in order to get from the current positioning
             * to the target.
             */
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF {
                val visiblePosition = anchor.position!!
                //determine scroll up or scroll down needed
                return PointF(0F, if (position > visiblePosition) 1F else -1F)
            }

            override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {
                super.onTargetFound(targetView, state, action)
                var dy = 0
                layoutManager?.apply {
                    val desiredTop = paddingTop
                    val currentTop = getDecoratedTop(targetView)
                    dy = currentTop - desiredTop
                }
                //perform fit animation to move target view at top of layout
                action.update(0, dy, timeMs, LinearInterpolator())
            }
        }
    }

    override fun canScrollVertically(): Boolean {
        canvas?.let { //TODO: Confirm entire code block should be in let
            it.findBorderViews()
            layoutManager.apply {
                if (childCount > 0) {
                    val top = getDecoratedTop(it.getTopView()!!)
                    val bottom = getDecoratedBottom(it.getBottomView()!!)
                    if (it.getMinPositionOnScreen() == 0 && it.getMaxPositionOnScreen() == itemCount - 1
                        && top >= paddingTop && bottom <= height - paddingBottom) {
                        return false
                    }
                } else {
                    return false
                }
            }
        }
        return layoutManager.isScrollingEnabledContract()

    }

    override fun canScrollHorizontally() = false

    override fun offsetChildren(d: Int) {
        layoutManager.offsetChildrenVertical(d)
    }
}
