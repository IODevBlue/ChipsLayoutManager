package com.blueiobase.api.android.chiplayoutmanager.gravity

import android.view.Gravity

import com.blueiobase.api.android.chiplayoutmanager.annotation.SpanLayoutChildGravity
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IChildGravityResolver

class CenterChildGravity: IChildGravityResolver {

    @SpanLayoutChildGravity
    override fun getItemGravity(position: Int) = Gravity.CENTER_VERTICAL

}