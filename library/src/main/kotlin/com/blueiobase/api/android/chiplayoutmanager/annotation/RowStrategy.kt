package com.blueiobase.api.android.chiplayoutmanager.annotation

import androidx.annotation.IntDef
import com.blueiobase.api.android.chiplayoutmanager.main.ChipLayoutManager


@IntDef(
    ChipLayoutManager.STRATEGY_DEFAULT,
    ChipLayoutManager.STRATEGY_FILL_SPACE,
    ChipLayoutManager.STRATEGY_FILL_VIEW,
    ChipLayoutManager.STRATEGY_CENTER,
    ChipLayoutManager.STRATEGY_CENTER_DENSE
)
@Retention(AnnotationRetention.SOURCE)
/**
 * Denotes that the annotated [Int] is one of the constants representing a [RowStrategy] for a [ChipLayoutManager].
 */
annotation class RowStrategy
