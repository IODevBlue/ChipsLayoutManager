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


class HorizontalScrollingController(private val layoutManager: ChipsLayoutManager, stateFactory: IStateFactory, scrollerListener: IScrollerListener):
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
                return PointF(if (position > visiblePosition) 1F else -1F, 0F)
            }

            override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {
                super.onTargetFound(targetView, state, action)
                val currentLeft = layoutManager!!.paddingLeft
                val desiredLeft = layoutManager!!.getDecoratedLeft(targetView)
                val dx = desiredLeft - currentLeft

                //perform fit animation to move target view at top of layoutX
                action.update(dx, 0, timeMs, LinearInterpolator())
            }
        }
    }

    override fun canScrollVertically() =  false

    override fun canScrollHorizontally(): Boolean {
        canvas!!.findBorderViews()
        if (layoutManager.childCount > 0) {
            val left = layoutManager.getDecoratedLeft(canvas!!.getLeftView()!!)
            val right = layoutManager.getDecoratedRight(canvas!!.getRightView()!!)
            if (canvas!!.getMinPositionOnScreen() == 0 && canvas!!.getMaxPositionOnScreen() == layoutManager.itemCount - 1 && left >= layoutManager.paddingLeft && right <= layoutManager.width - layoutManager.paddingRight) {
                return false
            }
        } else {
            return false
        }
        return layoutManager.isScrollingEnabledContract()
    }

    override fun offsetChildren(d: Int) {
        layoutManager.offsetChildrenHorizontal(d)
    }
}