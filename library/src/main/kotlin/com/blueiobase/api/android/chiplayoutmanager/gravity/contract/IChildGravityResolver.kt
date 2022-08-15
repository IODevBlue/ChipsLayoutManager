package com.blueiobase.api.android.chiplayoutmanager.gravity.contract

import com.blueiobase.api.android.chiplayoutmanager.annotation.SpanLayoutChildGravity

/** class which determines child gravity inside row from child position */
interface IChildGravityResolver {

    @SpanLayoutChildGravity
    fun getItemGravity(position: Int): Int
}