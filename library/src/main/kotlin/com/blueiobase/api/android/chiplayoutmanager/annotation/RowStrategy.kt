package com.blueiobase.api.android.chiplayoutmanager.annotation

import androidx.annotation.IntDef
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager


@IntDef(
    ChipsLayoutManager.STRATEGY_DEFAULT,
    ChipsLayoutManager.STRATEGY_FILL_SPACE,
    ChipsLayoutManager.STRATEGY_FILL_VIEW,
    ChipsLayoutManager.STRATEGY_CENTER,
    ChipsLayoutManager.STRATEGY_CENTER_DENSE
)
@Retention(AnnotationRetention.SOURCE)
/**
 * Denotes that the annotated [Int] is one of the constants representing a Row Strategy for a [ChipsLayoutManager].
 */
annotation class RowStrategy
