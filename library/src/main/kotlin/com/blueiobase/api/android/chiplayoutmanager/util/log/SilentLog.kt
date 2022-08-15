package com.blueiobase.api.android.chiplayoutmanager.util.log

internal class SilentLog : LogWrapper {

    override fun d(tag: String, msg: String): Int {
        //no op
        return 0
    }

    override fun v(tag: String, msg: String): Int {
        //no op
        return 0
    }

    override fun w(tag: String, msg: String): Int {
        //no op
        return 0
    }

    override fun i(tag: String, msg: String): Int {
        //no op
        return 0
    }

    override fun e(tag: String, msg: String): Int {
        //no op
        return 0
    }
}