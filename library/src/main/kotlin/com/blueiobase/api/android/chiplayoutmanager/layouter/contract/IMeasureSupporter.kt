package com.blueiobase.api.android.chiplayoutmanager.layouter.contract

import androidx.recyclerview.widget.RecyclerView

interface IMeasureSupporter {
    fun onItemsRemoved(recyclerView: RecyclerView)

    fun onSizeChanged()

    fun measure(autoWidth: Int, autoHeight: Int)

    fun getMeasuredWidth(): Int

    fun getMeasuredHeight(): Int

    fun isRegistered(): Boolean

    fun setRegistered(isRegistered: Boolean)
}