package com.zj.windmill.util

import android.util.Log
import com.blankj.utilcode.util.AppUtils

object Logger {

    private const val TAG = "Windmill"

    fun e(throwable: Throwable? = null, msg: String?, tag: String? = null) {
        if (AppUtils.isAppDebug()) return
        Log.e(tag ?: TAG, msg, throwable)
    }

    fun i(msg: String?, tag: String? = null) {
        if (!AppUtils.isAppDebug()) return
        Log.i(tag ?: TAG, msg ?: "")
    }
}