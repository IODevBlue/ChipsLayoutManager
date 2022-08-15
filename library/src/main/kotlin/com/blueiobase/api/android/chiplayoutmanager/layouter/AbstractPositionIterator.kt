package com.blueiobase.api.android.chiplayoutmanager.layouter

import androidx.annotation.IntRange

abstract class AbstractPositionIterator (@IntRange(from = 0) private val itemCount: Int): Iterator<Int>{

    var pos = 0

    init {
        require(itemCount > 0) { "Item count cannot be negative" }
    }


    open fun move(@IntRange(from = 0) pos: Int) {
        require(pos < itemCount) { "you can't move above of maxItemCount" }
        require(pos >= 0) { "can't move to negative position" }
        this.pos = pos
    }

    open fun remove() {
        throw UnsupportedOperationException("removing not supported in position iterator")
    }
}