package com.blueiobase.api.android.chiplayoutmanager.main.contract

import com.blueiobase.api.android.chiplayoutmanager.annotation.Orientation

interface IStateHolder {
    fun isLayoutRTL(): Boolean

    @Orientation
    fun layoutOrientation(): Int
}