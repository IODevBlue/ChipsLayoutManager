package com.blueiobase.api.android.chiplayoutmanager.gravity

import android.graphics.Rect
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifier

class CenterInColumnGravityModifier: IGravityModifier {

    override fun modifyChildRect(minTop: Int, maxBottom: Int, childRect: Rect): Rect {
        var childRect1 = childRect
        childRect1 = Rect(childRect1)
        childRect1.apply {
            val placeWidth = maxBottom - minTop
            val rectWidth: Int = right - left
            val halfOffset = (placeWidth - rectWidth) / 2
            left = minTop + halfOffset
            right = maxBottom - halfOffset
        }
        return childRect1
    }
}