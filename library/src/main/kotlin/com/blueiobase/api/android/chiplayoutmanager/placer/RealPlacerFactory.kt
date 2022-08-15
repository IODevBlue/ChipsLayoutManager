package com.blueiobase.api.android.chiplayoutmanager.placer

import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacer
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacerFactory

class RealPlacerFactory(private val layoutManager: RecyclerView.LayoutManager) : IPlacerFactory {

    override fun getAtStartPlacer(): IPlacer {
        return RealAtStartPlacer(layoutManager)
    }

    override fun getAtEndPlacer(): IPlacer {
        return RealAtEndPlacer(layoutManager)
    }
}