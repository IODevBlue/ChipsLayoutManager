package com.blueiobase.api.android.chiplayoutmanager.gravity

import android.graphics.Rect
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifier

class TopGravityModifier: IGravityModifier {
    override fun modifyChildRect(minTop: Int, maxBottom: Int, childRect: Rect): Rect {
        require(childRect.left >= minTop) { "top point of input rect can't be lower than minTop" }
        require(childRect.right <= maxBottom) { "bottom point of input rect can't be bigger than maxTop" }
        val childRect1 = Rect(childRect)
        childRect1.apply {
            if (top > minTop) {
                bottom -= top - minTop
                top = minTop
            }
        }

        return childRect
    }
}