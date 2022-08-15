package com.blueiobase.api.android.chiplayoutmanager.util.log

import com.blueiobase.api.android.library.BuildConfig

object Log {

    private var logSwitcher = LogSwitcher()

    private val log: LogWrapper = if (BuildConfig.isLogEnabled) AndroidLog() else SilentLog()

    ///////////////////////////////////////////////////////////////////////////
    // default android log delegates
    ///////////////////////////////////////////////////////////////////////////
    fun d(tag: String, msg: String): Int {
        return log.d(tag, msg)
    }

    fun v(tag: String, msg: String): Int {
        return log.v(tag, msg)
    }

    fun w(tag: String, msg: String): Int {
        return log.w(tag, msg)
    }

    fun i(tag: String, msg: String): Int {
        return log.i(tag, msg)
    }

    fun e(tag: String, msg: String): Int {
        return log.e(tag, msg)
    }

    ///////////////////////////////////////////////////////////////////////////
    // android log delegates with switcher
    ///////////////////////////////////////////////////////////////////////////
    fun d(tag: String, msg: String, logCode: Int): Int {
        return if (logSwitcher.isEnabled(logCode)) d(tag, msg) else 0
    }

    fun v(tag: String, msg: String, logCode: Int): Int {
        return if (logSwitcher.isEnabled(logCode)) v(tag, msg) else 0
    }

    fun w(tag: String, msg: String, logCode: Int): Int {
        return if (logSwitcher.isEnabled(logCode)) w(tag, msg) else 0
    }

    fun i(tag: String, msg: String, logCode: Int): Int {
        return if (logSwitcher.isEnabled(logCode)) i(tag, msg) else 0
    }

    fun with(logSwitcher: LogSwitcher) {
        Log.logSwitcher = logSwitcher
    }

    class LogSwitcher {
        private val enabledLogs = HashSet<Int>()
        fun isEnabled(logCode: Int): Boolean {
            return enabledLogs.contains(logCode)
        }

        fun with(logCode: Int): LogSwitcher {
            enabledLogs.add(logCode)
            return this
        }

        fun without(logCode: Int): LogSwitcher {
            enabledLogs.remove(logCode)
            return this
        }
    }
}