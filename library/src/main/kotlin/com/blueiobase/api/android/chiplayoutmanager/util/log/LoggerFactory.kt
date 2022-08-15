package com.blueiobase.api.android.chiplayoutmanager.util.log

import android.util.SparseArray
import android.view.View

class LoggerFactory {
    fun getFillLogger(viewCache: SparseArray<View>): IFillLogger {
        return FillLogger(viewCache)
    }
}