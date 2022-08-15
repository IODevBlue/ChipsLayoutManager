package com.blueiobase.api.android.chiplayoutmanager.placer

import android.view.View
import androidx.recyclerview.widget.RecyclerView


class DisappearingViewAtStartPlacer(layoutManager: RecyclerView.LayoutManager): AbstractPlacer(layoutManager) {
    override fun addView(view: View) {
        layoutManager.addDisappearingView(view, 0)
    }
}