package com.blueiobase.api.android.chiplayoutmanager.main.contract

interface IScrollingContract {
    fun setScrollingEnabledContract(isEnabled: Boolean)

    fun isScrollingEnabledContract(): Boolean

    fun setSmoothScrollbarEnabled(enabled: Boolean)

    fun isSmoothScrollbarEnabled(): Boolean
}