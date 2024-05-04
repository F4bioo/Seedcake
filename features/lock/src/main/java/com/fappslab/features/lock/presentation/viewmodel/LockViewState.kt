package com.fappslab.features.lock.presentation.viewmodel

import com.fappslab.features.common.navigation.ScreenTypeArgs
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.ScreenType

internal data class LockViewState(
    val args: ScreenTypeArgs,
    val screenType: ScreenType? = null,
    val isPinValidated: Boolean = false,
    val shouldShufflePin: Boolean = true,
    val shouldShowPinLockWarning: Boolean = true,
    val shouldShowFingerPrintButton: Boolean = false
) {

    fun showLockScreen(screenType: ScreenType?, shouldShufflePin: Boolean) = copy(
        shouldShowPinLockWarning = screenType.toPinLockWarningVisibility(),
        shouldShufflePin = shouldShufflePin,
        screenType = screenType
    )

    private fun ScreenType?.toPinLockWarningVisibility(): Boolean =
        (this is ScreenType.Register) && (args == ScreenTypeArgs.PreferencesRegister)
}
