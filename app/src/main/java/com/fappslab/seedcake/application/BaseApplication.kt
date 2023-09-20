package com.fappslab.seedcake.application

import android.app.Application
import com.fappslab.features.common.navigation.LockNavigation
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class BaseApplication : Application() {

    private val lockNavigation: LockNavigation by inject()
    private val lockIntent by lazy { lockNavigation.createServiceIntent(this) }
    private val activityCallbacks by lazy {
        ActivityCallbacks(
            startServiceBlock = { startService(lockIntent) },
            stopServiceBlock = { stopService(lockIntent) }
        )
    }

    override fun onCreate() {
        super.onCreate()
        startKoin()
        registerActivityLifecycleCallbacks(activityCallbacks)
    }

    override fun onTerminate() {
        stopKoin()
        unregisterActivityLifecycleCallbacks(activityCallbacks)
        super.onTerminate()
    }

    private fun startKoin() {
        startKoin(KoinDeclaration.get(this))
    }
}
