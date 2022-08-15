package com.blueiobase.api.android.chiplayoutmanager.util

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.IStateFactory

object StateHelper {
    fun isInfinite(stateFactory: IStateFactory): Boolean {
        return (stateFactory.getSizeMode() == View.MeasureSpec.UNSPECIFIED
                && stateFactory.getEnd() == 0)
    }
}