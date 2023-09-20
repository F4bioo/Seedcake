package com.fappslab.seedcake.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.fappslab.features.common.domain.usecase.GetScreenShieldStateUseCase
import com.fappslab.features.lock.presentation.LockActivity
import com.fappslab.libraries.security.extension.screenShield
import com.fappslab.seedcake.presentation.MainActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ActivityCallbacks(
    private val startServiceBlock: () -> Unit = {},
    private val stopServiceBlock: () -> Unit = {}
) : KoinComponent, Application.ActivityLifecycleCallbacks {

    private val getScreenShieldState: GetScreenShieldStateUseCase by inject()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity.screenShield(isEnabled = getScreenShieldState())
    }

    override fun onActivityStarted(activity: Activity) {
        when (activity) {
            is LockActivity -> startServiceBlock()
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        when (activity) {
            is MainActivity -> stopServiceBlock()
        }
    }

    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
}
