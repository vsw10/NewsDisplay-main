package com.husqvarna.newsapp.logs

import android.util.Log

object NDLogs {

    private const val TAG = "nd"

    internal var enableLogger: Boolean = true

    fun debug(tag: String, message: String, enableLogger: Boolean = NDLogs.enableLogger) {

        if (enableLogger) Log.d(TAG, "$tag $message")
    }

    fun error(
        tag: String,
        message: String,
        error: Throwable,
        enableLogger: Boolean = NDLogs.enableLogger
    ) {

        if (enableLogger) Log.e(TAG, "$tag $message")
    }

    fun info(tag: String, message: String, enableLogger: Boolean = NDLogs.enableLogger) {

        if (enableLogger) Log.i(TAG, "$tag $message")
    }

    fun trace(tag: String, message: String, enableLogger: Boolean = NDLogs.enableLogger) {

        if (enableLogger) Log.v(TAG, "$tag $message")
    }

    fun warn(tag: String, message: String, enableLogger: Boolean = NDLogs.enableLogger) {

        if (enableLogger) Log.w(TAG, "$tag $message")
    }
}