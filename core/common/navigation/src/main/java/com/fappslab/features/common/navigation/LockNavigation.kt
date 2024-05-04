package com.fappslab.features.common.navigation

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val EXTRA_LOCK_RESULT = "EXTRA_LOCK_RESULT"

interface LockNavigation {
    fun createLockIntent(context: Context, args: ScreenTypeArgs): Intent
}

@Parcelize
enum class ScreenTypeArgs : Parcelable {
    LockScreen,
    PreferencesValidate,
    PreferencesRegister,
}
