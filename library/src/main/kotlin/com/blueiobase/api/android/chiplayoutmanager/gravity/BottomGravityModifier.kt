package com.blueiobase.api.android.chiplayoutmanager.gravity

import android.graphics.Rect
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifier

class BottomGravityModifier: IGravityModifier {

    override fun modifyChildRect(minTop: Int, maxBottom: Int, childRect: Rect): Rect {
        require(childRect.top >= minTop) { "top point of input rect can't be lower than minTop" }
        require(childRect.bottom <= maxBottom) { "bottom point of input rect can't be bigger than maxTop" }
        val modified = Rect(childRect)
        modified.apply {
            if (bottom < maxBottom) {
                top += maxBottom - bottom
                bottom = maxBottom
            }
        }

        return modified
    }
}