package com.blueiobase.api.android.chiplayoutmanager.layouter.model

import android.graphics.Rect

data class Item(val viewRect: Rect, val viewPosition: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        //val (_, viewPosition1) = other as Item
        val item = other as Item
        return viewPosition == item.viewPosition
        //return viewPosition === viewPosition1
    }

    override fun hashCode() = viewPosition

}
