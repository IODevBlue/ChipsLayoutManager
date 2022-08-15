package com.blueiobase.api.android.chiplayoutmanager.placer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacer

class RealAtStartPlacer(layoutManager: RecyclerView.LayoutManager): AbstractPlacer(layoutManager),
    IPlacer {

    override fun addView(view: View) {
        //mark that we add view at beginning of children
        layoutManager.addView(view, 0)

    }
}