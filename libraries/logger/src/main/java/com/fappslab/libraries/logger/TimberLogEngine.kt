package com.fappslab.libraries.logger

import com.fappslab.seedcake.libraries.logger.BuildConfig
import timber.log.Timber

internal class TimberLogEngine : LogEngine {

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun debug(message: String?) {
        Timber.d(message)
    }

    override fun info(message: String?) {
        Timber.i(message)
    }

    override fun warning(message: String?) {
        Timber.w(message)
    }

    override fun error(message: String?) {
        Timber.e(message)
    }
}
