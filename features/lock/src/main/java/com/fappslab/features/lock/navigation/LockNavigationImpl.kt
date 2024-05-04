package com.fappslab.features.lock.navigation

import android.content.Context
import android.content.Intent
import com.fappslab.features.common.navigation.LockNavigation
import com.fappslab.features.common.navigation.ScreenTypeArgs
import com.fappslab.features.lock.presentation.LockActivity

internal class LockNavigationImpl : LockNavigation {

    override fun createLockIntent(context: Context, args: ScreenTypeArgs): Intent =
        LockActivity.createIntent(context, args)
}
