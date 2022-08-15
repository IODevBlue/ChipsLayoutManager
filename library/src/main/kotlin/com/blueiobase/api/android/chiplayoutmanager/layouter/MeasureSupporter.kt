package com.blueiobase.api.android.chiplayoutmanager.layouter

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IMeasureSupporter


class MeasureSupporter (private val lm: RecyclerView.LayoutManager): AdapterDataObserver(),
    IMeasureSupporter {
    var isAfterRemoving = false
        private set
    private var measuredWidth = 0
    private var measuredHeight = 0
    private var isRegistered = false

    /**
     * width of RecyclerView before removing item
     */
    private var beforeRemovingWidth: Int? = null

    /**
     * width which we receive after [RecyclerView.LayoutManager.onLayoutChildren] method finished.
     * Contains correct width after auto-measuring
     */
    private var autoMeasureWidth = 0

    /**
     * height of RecyclerView before removing item
     */
    private var beforeRemovingHeight: Int? = null

    /**
     * height which we receive after [RecyclerView.LayoutManager.onLayoutChildren] method finished.
     * Contains correct height after auto-measuring
     */
    private var autoMeasureHeight = 0
    override fun onSizeChanged() {
        autoMeasureWidth = lm.width
        autoMeasureHeight = lm.height
    }

    override fun getMeasuredWidth(): Int {
        return measuredWidth
    }

    private fun setMeasuredWidth(measuredWidth: Int) {
        this.measuredWidth = measuredWidth
    }

    override fun getMeasuredHeight(): Int {
        return measuredHeight
    }

    override fun isRegistered(): Boolean {
        return isRegistered
    }

    override fun setRegistered(isRegistered: Boolean) {
        this.isRegistered = isRegistered
    }

    private fun setMeasuredHeight(measuredHeight: Int) {
        this.measuredHeight = measuredHeight
    }

    @CallSuper
    override fun measure(autoWidth: Int, autoHeight: Int) {
        if (isAfterRemoving) {
            setMeasuredWidth(autoWidth.coerceAtLeast(beforeRemovingWidth!!))
            setMeasuredHeight(autoHeight.coerceAtLeast(beforeRemovingHeight!!))
        } else {
            setMeasuredWidth(autoWidth)
            setMeasuredHeight(autoHeight)
        }
    }

    override fun onItemsRemoved(recyclerView: RecyclerView) {
        //subscribe to next animations tick
        lm.postOnAnimation(object : Runnable {
            private fun onFinished() {
                //when removing animation finished return auto-measuring back
                isAfterRemoving = false
                // and process measure again
                lm.requestLayout()
            }

            override fun run() {
                if (recyclerView.itemAnimator != null) {
                    //listen removing animation
                    recyclerView.itemAnimator!!.isRunning { onFinished() }
                } else {
                    onFinished()
                }
            }
        })
    }

    @CallSuper
    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        /** we detected removing event, so should process measuring manually
         * @see [Stack Overflow issue](http://stackoverflow.com/questions/40242011/custom-recyclerviews-layoutmanager-automeasuring-after-animation-finished-on-i)
         */
        isAfterRemoving = true
        beforeRemovingWidth = autoMeasureWidth
        beforeRemovingHeight = autoMeasureHeight
    }
}