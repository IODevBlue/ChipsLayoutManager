package com.blueiobase.api.android.chiplayoutmanager.util.log


class AndroidLog : LogWrapper {
    override fun d(tag: String, msg: String): Int {
        return Log.d(tag, msg)
    }

    override fun v(tag: String, msg: String): Int {
        return Log.v(tag, msg)
    }

    override fun w(tag: String, msg: String): Int {
        return Log.w(tag, msg)
    }

    override fun i(tag: String, msg: String): Int {
        return Log.i(tag, msg)
    }

    override fun e(tag: String, msg: String): Int {
        return Log.e(tag, msg)
    }
}