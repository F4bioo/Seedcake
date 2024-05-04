package com.fappslab.features.preferences.presentation.viewmodel

import com.fappslab.features.common.navigation.ScreenTypeArgs

internal sealed class PreferencesViewAction {
    data object BiometricPrompt : PreferencesViewAction()
    data class ScreenShield(val isChecked: Boolean) : PreferencesViewAction()
    data class ShowLockScreen(val args: ScreenTypeArgs) : PreferencesViewAction()
}
