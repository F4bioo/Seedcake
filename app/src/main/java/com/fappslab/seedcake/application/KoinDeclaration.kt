package com.fappslab.seedcake.application

import android.app.Application
import com.fappslab.seedcake.BuildConfig
import com.fappslab.seedcake.libraries.arch.koin.koinshot.ModuleInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.dsl.KoinAppDeclaration

object KoinDeclaration {

    fun get(application: Application): KoinAppDeclaration = {
        androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
        androidContext(application)
        setupKoinShot()
    }

    private fun KoinApplication.setupKoinShot() {
        modules(ModuleInitializer.modules.toList())
    }
}
