package com.blueiobase.api.android.chiplayoutmanager.layouter


import androidx.recyclerview.widget.RecyclerView

class RowSquare(private val layoutManager: RecyclerView.LayoutManager) : Square(layoutManager) {

    override fun getCanvasRightBorder(): Int {
        return layoutManager.width - layoutManager.paddingRight
    }

    /** get bottom border. Controlled by clipToPadding property */
    override fun getCanvasBottomBorder(): Int {
        return layoutManager.height
    }

    override fun getCanvasLeftBorder(): Int {
        return layoutManager.paddingLeft
    }

    /** get bottom border. Controlled by clipToPadding property */
    override fun getCanvasTopBorder(): Int {
        return 0
    }
}