package com.blueiobase.api.android.chiplayoutmanager.gravity

import android.graphics.Rect
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifier

class RightGravityModifier: IGravityModifier {

    override fun modifyChildRect(minTop: Int, maxBottom: Int, childRect: Rect): Rect {
        val childRect1 = Rect(childRect)
        childRect1.apply {
            if (right < maxBottom) {
                left += maxBottom - right
                right = maxBottom
            }
        }
        return childRect
    }
}