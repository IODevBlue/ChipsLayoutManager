package com.blueiobase.api.android.chiplayoutmanager.annotation

import android.view.Gravity
import android.view.View
import androidx.annotation.IntDef
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager

@IntDef(Gravity.TOP,
    Gravity.BOTTOM,
    Gravity.CENTER,
    Gravity.CENTER_VERTICAL,
    Gravity.CENTER_HORIZONTAL,
    Gravity.LEFT,
    Gravity.RIGHT,
    Gravity.FILL)
@Retention(AnnotationRetention.SOURCE)

/**
 * Denotes that the annotated [Int] is a constant representing the [Gravity] to be applied to [View] objects
 * managed by a [ChipsLayoutManager].
 */
annotation class SpanLayoutChildGravity
