package com.blueiobase.api.android.chiplayoutmanager.placer.contract

interface IPlacerFactory {
    fun getAtStartPlacer(): IPlacer
    fun getAtEndPlacer(): IPlacer
}