package com.blueiobase.api.android.chiplayoutmanager.gravity

import android.graphics.Rect
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifier

class CenterInRowGravityModifier: IGravityModifier {

    override fun modifyChildRect(minTop: Int, maxBottom: Int, childRect: Rect): Rect {
        var childRect1: Rect = childRect
        require(childRect1.top >= minTop) { "top point of input rect can't be lower than minTop" }
        require(childRect1.bottom <= maxBottom) { "bottom point of input rect can't be bigger than maxTop" }
        childRect1 = Rect(childRect1)
        val placeHeight = maxBottom - minTop
        childRect1.apply {
            val rectHeight: Int = bottom - top
            //calculate needed offset
            val halfOffset = (placeHeight - rectHeight) / 2
            top = minTop + halfOffset
            bottom = maxBottom - halfOffset
        }
        return childRect1
    }
}