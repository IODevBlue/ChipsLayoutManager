package com.blueiobase.api.android.chiplayoutmanager.gravity

import com.blueiobase.api.android.chiplayoutmanager.annotation.SpanLayoutChildGravity
import com.blueiobase.api.android.chiplayoutmanager.gravity.contract.IChildGravityResolver

class CustomGravityResolver(@SpanLayoutChildGravity private val gravity: Int):
    IChildGravityResolver {

    @SpanLayoutChildGravity
    override fun getItemGravity(position: Int) = gravity

}