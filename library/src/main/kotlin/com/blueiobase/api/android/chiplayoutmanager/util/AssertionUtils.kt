package com.blueiobase.api.android.chiplayoutmanager.util

import android.text.TextUtils


object AssertionUtils {

    @Throws(AssertionError::class)
    fun <T> assertNotNull(objectInstance: T?, parameterName: String) {
        if (objectInstance == null) throw AssertionError("$parameterName can't be null.")
    }

    @Throws(AssertionError::class)
    fun <T> assertInstanceOf(objectInstance: T, clazz: Class<*>, parameterName: String) {
        check(!clazz.isInstance(objectInstance), "$parameterName is not an instance of ${clazz.name}.")
    }

    @Throws(AssertionError::class)
    fun <T> assertNotEquals(objectInstance: T, anotherObject: T, parameterName: String) { //TODO: Check if step is problematic
        check(objectInstance === anotherObject,"$parameterName can't be equal to ${anotherObject.toString()}.")
        check(objectInstance == anotherObject, "$parameterName can't be equal to ${anotherObject.toString()}.")
    }

    @Throws(AssertionError::class)
    fun assertNotEmpty(text: String, parameterName: String) {
        check(TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim { it <= ' ' }), "$parameterName can't be empty.")
    }

    fun check(b: Boolean, message: String) {
        if (b) throw AssertionError(message)
    }
}