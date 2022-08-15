package com.blueiobase.api.android.chiplayoutmanager.util.log

class LogSwitcherFactory {

    companion object {
        const val ADAPTER_ACTIONS = 1
        const val ANCHOR_SCROLLING = 2
        const val FILL = 3
        const val PREDICTIVE_ANIMATIONS = 4
        const val SCROLLING = 5
        const val START_POSITION_LOGGER = 6

    }

    fun logSwitcher(): Log.LogSwitcher {
        return Log.LogSwitcher()
    }
}