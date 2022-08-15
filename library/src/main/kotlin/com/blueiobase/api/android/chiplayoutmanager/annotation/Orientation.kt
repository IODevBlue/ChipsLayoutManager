package com.blueiobase.api.android.chiplayoutmanager.annotation

import androidx.annotation.IntDef
import com.blueiobase.api.android.chiplayoutmanager.main.ChipLayoutManager

@IntDef(ChipLayoutManager.HORIZONTAL, ChipLayoutManager.VERTICAL)

/**
 * Denotes that the annotated [Int] is a constant representing the orientation of the [ChipLayoutManager].
 */
annotation class Orientation
