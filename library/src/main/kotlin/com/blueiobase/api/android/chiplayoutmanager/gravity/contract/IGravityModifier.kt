package com.blueiobase.api.android.chiplayoutmanager.gravity.contract

import android.graphics.Rect
import androidx.annotation.IntRange

interface IGravityModifier {

    /** @return created rect based on modified input rect due to concrete gravity modifier.
     * @param childRect input rect. Immutable
     */
    fun modifyChildRect(@IntRange(from = 0) minTop: Int, @IntRange(from = 0) maxBottom: Int, childRect: Rect): Rect

}