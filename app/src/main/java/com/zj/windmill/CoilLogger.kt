package com.zj.windmill

import android.util.Log.DEBUG
import coil.util.Logger
import timber.log.Timber

class CoilLogger : Logger {

    override var level: Int = DEBUG

    override fun log(tag: String, priority: Int, message: String?, throwable: Throwable?) {
        if (message != null) {
            Timber.i(message)
            if (throwable != null) {
                Timber.e(throwable, message)
            }
        }
    }
}