package com.blueiobase.api.android.chiplayoutmanager.layouter

import androidx.annotation.IntRange

class DecrementalPositionIterator(@IntRange(from = 0) itemCount: Int): AbstractPositionIterator(itemCount) {

    override fun hasNext() = pos >= 0

    override fun next(): Int {
        check(hasNext()) { "position out of bounds reached" }
        return pos--
    }
}
