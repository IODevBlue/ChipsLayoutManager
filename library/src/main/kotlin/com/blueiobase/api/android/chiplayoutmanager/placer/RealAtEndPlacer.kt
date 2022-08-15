package com.blueiobase.api.android.chiplayoutmanager.placer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacer


class RealAtEndPlacer(layoutManager: RecyclerView.LayoutManager): AbstractPlacer(layoutManager),
    IPlacer {

    override fun addView(view: View) {
        layoutManager.addView(view)

    }
}