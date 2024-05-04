package com.fappslab.features.preferences.presentation.extension

import android.content.Context
import androidx.biometric.BiometricPrompt
import com.fappslab.features.preferences.presentation.PreferencesFragment
import com.fappslab.seedcake.features.preferences.R
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.PinResult
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.ScreenType
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.lock.plutoLockPage
import com.fappslab.seedcake.libraries.extension.BiometricParams
import com.fappslab.seedcake.libraries.extension.createBiometricDialog

private const val TAG_LOCK_PAGE = "showLockPage"

internal fun PreferencesFragment.showLockPage(
    screenType: ScreenType?,
    backBlock: () -> Unit,
    validationBlock: (PinResult) -> Unit
) {
    screenType?.run {
        plutoLockPage(screenType = this) {
            onBackPressed = backBlock
            onValidationResult = { result ->
                validationBlock(result)
                dismissAllowingStateLoss()
            }
        }
    }?.build(manager = childFragmentManager, tag = TAG_LOCK_PAGE)
}

internal fun Context.createBiometricDialog(): BiometricPrompt.PromptInfo {
    val params = BiometricParams(
        titleTextRes = R.string.common_unlock_title,
        appNameTextRes = R.string.app_name,
        cancelButtonTextRes = R.string.common_close
    )
    return createBiometricDialog(params)
}
