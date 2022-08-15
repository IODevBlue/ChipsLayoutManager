package com.blueiobase.api.android.chiplayoutmanager.main.contract

interface IPositionsContract {
    fun findFirstVisibleItemPosition(): Int
    fun findFirstCompletelyVisibleItemPosition(): Int
    fun findLastVisibleItemPosition(): Int
    fun findLastCompletelyVisibleItemPosition(): Int
}