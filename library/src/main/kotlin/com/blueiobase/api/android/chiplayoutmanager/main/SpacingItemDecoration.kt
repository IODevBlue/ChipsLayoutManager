package com.blueiobase.api.android.chiplayoutmanager.main

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class SpacingItemDecoration(private val horizontalSpacing: Int, private val verticalSpacing: Int): ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.apply {
            left = horizontalSpacing / 2
            right = horizontalSpacing / 2
            top = verticalSpacing / 2
            bottom = verticalSpacing / 2

        }
    }
}