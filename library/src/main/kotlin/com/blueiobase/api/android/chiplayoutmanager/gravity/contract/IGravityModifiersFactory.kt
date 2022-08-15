package com.blueiobase.api.android.chiplayoutmanager.gravity.contract

import com.blueiobase.api.android.chiplayoutmanager.annotation.SpanLayoutChildGravity

interface IGravityModifiersFactory {
    fun getGravityModifier(@SpanLayoutChildGravity gravity: Int): IGravityModifier
}