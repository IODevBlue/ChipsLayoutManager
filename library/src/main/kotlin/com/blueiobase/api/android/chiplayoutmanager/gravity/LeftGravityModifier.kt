package com.blueiobase.api.android.chiplayoutmanager.gravity

import android.graphics.Rect
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifier

class LeftGravityModifier: IGravityModifier {

    override fun modifyChildRect(minTop: Int, maxBottom: Int, childRect: Rect): Rect {
        val childRect1 = Rect(childRect)
        childRect1.apply{
            if (left > minTop) {
                right -= left - minTop
                left = minTop
            }
        }

        return childRect1
    }
}