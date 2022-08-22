package com.blueiobase.api.android.chiplayoutmanager.placer

import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager
import com.blueiobase.api.android.chiplayoutmanager.placer.contract.IPlacerFactory


class PlacerFactory(private val lm: ChipsLayoutManager) {
    fun createRealPlacerFactory(): IPlacerFactory {
        return RealPlacerFactory(lm)
    }

    fun createDisappearingPlacerFactory(): IPlacerFactory {
        return DisappearingPlacerFactory(lm)
    }
}