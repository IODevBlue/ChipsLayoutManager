package com.blueiobase.api.android.chiplayoutmanager.layouter

import androidx.annotation.IntRange

class IncrementalPositionIterator(@IntRange(from = 0) private val itemCount: Int): AbstractPositionIterator(itemCount) {

    override fun hasNext() = pos < itemCount

    override fun next(): Int {
        check(hasNext()) { "position out of bounds reached" }
        return pos++
    }
}