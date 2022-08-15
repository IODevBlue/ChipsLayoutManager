package com.blueiobase.api.android.chiplayoutmanager.gravity

import android.util.SparseArray
import android.view.Gravity
import com.blueiobase.api.android.chiplayoutmanager.annotation.SpanLayoutChildGravity
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifier
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifiersFactory

class ColumnGravityModifiersFactory: IGravityModifiersFactory {

    private val gravityModifierMap = SparseArray<IGravityModifier>()

    init {
        val centerGravityModifier = CenterInColumnGravityModifier()

        gravityModifierMap.apply {
            put(Gravity.CENTER, centerGravityModifier)
            put(Gravity.CENTER_HORIZONTAL, centerGravityModifier)
            put(Gravity.LEFT, LeftGravityModifier())
            put(Gravity.RIGHT, RightGravityModifier())
        }

    }

    override fun getGravityModifier(@SpanLayoutChildGravity gravity: Int): IGravityModifier {
        var gravityModifier = gravityModifierMap[gravity]
        if (gravityModifier == null) {
            gravityModifier = gravityModifierMap[Gravity.CENTER_HORIZONTAL]
        }
        return gravityModifier
    }

}