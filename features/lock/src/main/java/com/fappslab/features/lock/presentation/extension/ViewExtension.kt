package com.fappslab.features.lock.presentation.extension

import android.content.Context
import androidx.biometric.BiometricPrompt
import com.fappslab.seedcake.features.lock.R
import com.fappslab.seedcake.libraries.extension.BiometricParams
import com.fappslab.seedcake.libraries.extension.createBiometricDialog

internal fun Context.createBiometricDialog(): BiometricPrompt.PromptInfo {
    val params = BiometricParams(
        titleTextRes = R.string.common_unlock_title,
        appNameTextRes = R.string.app_name,
        cancelButtonTextRes = R.string.common_close
    )
    return createBiometricDialog(params)
}
