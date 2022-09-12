package com.blueiobase.api.android.chiplayoutmanager.annotation

import androidx.annotation.IntDef
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager

@IntDef(ChipsLayoutManager.HORIZONTAL, ChipsLayoutManager.VERTICAL)

/** Denotes that the annotated [Int] is a constant representing the orientation of the [ChipsLayoutManager]. */
annotation class Orientation
