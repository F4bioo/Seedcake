package com.fappslab.features.lock.presentation.viewmodel

import com.fappslab.features.common.navigation.ScreenTypeArgs
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.ScreenType

internal data class LockViewState(
    val args: ScreenTypeArgs,
    val screenType: ScreenType? = null,
    val isPinValidated: Boolean = false,
    val shouldShowPinLockWarning: Boolean = true,
    val shouldShowFingerPrintButton: Boolean = false
) {

    fun showLockScreen(screenType: ScreenType?) = copy(
        shouldShowPinLockWarning = screenType.toPinLockWarningVisibility(),
        screenType = screenType
    )

    private fun ScreenType?.toPinLockWarningVisibility(): Boolean =
        this is ScreenType.Register && args == ScreenTypeArgs.PreferencesRegister
}
