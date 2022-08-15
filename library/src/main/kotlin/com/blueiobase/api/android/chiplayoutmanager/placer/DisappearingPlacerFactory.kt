package com.blueiobase.api.android.chiplayoutmanager.placer

import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacer
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacerFactory


internal class DisappearingPlacerFactory(private val layoutManager: RecyclerView.LayoutManager) :
    IPlacerFactory {
    override fun getAtStartPlacer(): IPlacer {
        return DisappearingViewAtStartPlacer(layoutManager)
    }

    override fun getAtEndPlacer(): IPlacer {
        return DisappearingViewAtEndPlacer(layoutManager)
    }
}