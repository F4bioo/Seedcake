package com.fappslab.seedcake.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.fappslab.features.common.domain.usecase.GetScreenShieldStateUseCase
import com.fappslab.libraries.security.extension.screenShield
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ActivityLifecycle : KoinComponent, Application.ActivityLifecycleCallbacks {

    private val getScreenShieldState: GetScreenShieldStateUseCase by inject()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity.screenShield(isEnabled = getScreenShieldState())
    }

    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
}
