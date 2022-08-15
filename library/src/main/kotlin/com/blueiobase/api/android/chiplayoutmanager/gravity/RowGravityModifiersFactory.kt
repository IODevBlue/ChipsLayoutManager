package com.blueiobase.api.android.chiplayoutmanager.gravity

import android.util.SparseArray
import android.view.Gravity
import com.blueiobase.api.android.chiplayoutmanager.annotation.SpanLayoutChildGravity
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifier
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IGravityModifiersFactory

class RowGravityModifiersFactory: IGravityModifiersFactory {

    private val gravityModifierMap = SparseArray<IGravityModifier>()

    init {

        val centerInRowGravityModifier = CenterInRowGravityModifier()
        val topGravityModifier = TopGravityModifier()
        val bottomGravityModifier = BottomGravityModifier()

        gravityModifierMap.put(Gravity.TOP, topGravityModifier)
        gravityModifierMap.put(Gravity.BOTTOM, bottomGravityModifier)
        gravityModifierMap.put(Gravity.CENTER, centerInRowGravityModifier)
        gravityModifierMap.put(Gravity.CENTER_VERTICAL, centerInRowGravityModifier)

    }

    override fun getGravityModifier(@SpanLayoutChildGravity gravity: Int): IGravityModifier {
        var gravityModifier = gravityModifierMap[gravity]
        gravityModifier?.let {
            gravityModifier = gravityModifierMap[Gravity.CENTER_VERTICAL]
        }
        return gravityModifier
    }
}