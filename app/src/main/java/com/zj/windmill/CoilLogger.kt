package com.zj.windmill

import android.util.Log.DEBUG
import coil.util.Logger

private const val TAG = "Coil"

class CoilLogger : Logger {

    override var level: Int = DEBUG

    override fun log(tag: String, priority: Int, message: String?, throwable: Throwable?) {
        if (message != null) {
            com.zj.windmill.util.Logger.i(message, TAG)
            if (throwable != null) {
                com.zj.windmill.util.Logger.e(throwable, message, TAG)
            }
        }
    }
}