package com.zj.windmill

import android.util.Log.DEBUG
import coil.util.Logger

class CoilLogger : Logger {

    override var level: Int = DEBUG

    override fun log(tag: String, priority: Int, message: String?, throwable: Throwable?) {
        if (message != null) {
            com.orhanobut.logger.Logger.t("Coil")
                .i(message)
            if (throwable != null) {
                com.orhanobut.logger.Logger.t("Coil")
                    .e(throwable, message)
            }
        }
    }
}