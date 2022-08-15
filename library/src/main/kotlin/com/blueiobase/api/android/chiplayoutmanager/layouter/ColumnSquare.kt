package com.blueiobase.api.android.chiplayoutmanager.layouter

import androidx.recyclerview.widget.RecyclerView


internal class ColumnSquare(private val layoutManager: RecyclerView.LayoutManager) : Square(layoutManager) {

    override fun getCanvasRightBorder() = layoutManager.width

    override fun getCanvasBottomBorder() = layoutManager.height - layoutManager.paddingBottom

    override fun getCanvasLeftBorder() = 0

    override fun getCanvasTopBorder() = layoutManager.paddingTop
}