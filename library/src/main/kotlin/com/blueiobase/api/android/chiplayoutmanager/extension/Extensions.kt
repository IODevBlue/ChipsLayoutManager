package com.blueiobase.api.android.chiplayoutmanager.extension

import android.content.Context
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager

/**
 * Extension function to create a [ChipsLayoutManager]. There is no need to call the [build()][ChipsLayoutManager.Builder.build]
 * in this function, it is done internally.
 *
 */
fun Context.chipsLayoutManager(init:ChipsLayoutManager.Builder.()->Unit): ChipsLayoutManager {
    val builder = ChipsLayoutManager(this).StrategyBuilder()
    builder.init()
    return builder.build()
}