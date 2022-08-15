package com.blueiobase.api.android.chiplayoutmanager.breaker.contract

interface IBreakerFactory {

    fun createBackwardRowBreaker(): ILayoutRowBreaker

    fun createForwardRowBreaker(): ILayoutRowBreaker
}