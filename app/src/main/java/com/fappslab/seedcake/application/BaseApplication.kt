package com.fappslab.seedcake.application

import android.app.Application
import android.content.Intent
import com.fappslab.features.common.domain.usecase.GetPinStateUseCase
import com.fappslab.features.common.navigation.LockNavigation
import com.fappslab.features.common.navigation.ScreenTypeArgs
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class BaseApplication : Application() {

    private val lockNavigation: LockNavigation by inject()
    private val getPinStateUseCase: GetPinStateUseCase by inject()
    private val activityLifecycle by lazy { ActivityLifecycle() }
    private val applicationLifecycle by lazy { ApplicationLifecycle(::showLockScreenIfNeeded) }

    override fun onCreate() {
        super.onCreate()
        startKoin()
        registerActivityLifecycleCallbacks(activityLifecycle)
        applicationLifecycle.registerApplicationLifecycleCallbacks()
    }

    override fun onTerminate() {
        stopKoin()
        unregisterActivityLifecycleCallbacks(activityLifecycle)
        applicationLifecycle.unregisterApplicationLifecycleCallbacks()
        super.onTerminate()
    }

    private fun startKoin() {
        startKoin(KoinDeclaration.get(this))
    }

    private fun showLockScreenIfNeeded() {
        if (getPinStateUseCase()) {
            lockNavigation.createLockIntent(context = this, ScreenTypeArgs.LockScreen)
                .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP }
                .also(::startActivity)
        }
    }
}
