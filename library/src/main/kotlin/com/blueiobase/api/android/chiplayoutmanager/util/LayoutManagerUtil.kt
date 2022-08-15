package com.blueiobase.api.android.chiplayoutmanager.util

import androidx.recyclerview.widget.RecyclerView

object LayoutManagerUtil {

    /**
     * perform changing layout with playing RecyclerView animations
     */
    fun requestLayoutWithAnimations(layoutManager: RecyclerView.LayoutManager) {
        layoutManager.apply {
            postOnAnimation {
                requestLayout()
                requestSimpleAnimationsInNextLayout()
            }
        }
    }
}