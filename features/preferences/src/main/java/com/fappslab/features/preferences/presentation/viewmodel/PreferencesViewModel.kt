package com.fappslab.features.preferences.presentation.viewmodel

import androidx.biometric.BiometricPrompt
import com.fappslab.features.common.domain.usecase.GetFingerprintStateUseCase
import com.fappslab.features.common.domain.usecase.GetPinStateUseCase
import com.fappslab.features.common.domain.usecase.GetPinUseCase
import com.fappslab.features.common.navigation.ScreenTypeArgs
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.PinResult

internal class PreferencesViewModel(
    private val getPinUseCase: GetPinUseCase,
    private val getPinStateUseCase: GetPinStateUseCase,
    private val getFingerprintStateUseCase: GetFingerprintStateUseCase
) : ViewModel<PreferencesViewState, PreferencesViewAction>(PreferencesViewState()) {

    init {
        getSecurityCheckBoxPreference()
    }

    private fun getSecurityCheckBoxPreference() {
        val isCheckBoxPinChecked = getPinStateUseCase()
        val isCheckBoxFingerprintChecked = getFingerprintStateUseCase()
        onState { it.securityCheckBoxState(isCheckBoxPinChecked, isCheckBoxFingerprintChecked) }
    }

    fun onCheckBiometricAvailability(hasBiometric: Boolean) {
        onState { it.copy(shouldShowCheckBoxFingerprint = hasBiometric) }
    }

    fun onShowLockScreen() {
        val args = getPinUseCase()?.let {
            ScreenTypeArgs.PreferencesValidate
        } ?: ScreenTypeArgs.PreferencesRegister
        onAction { PreferencesViewAction.ShowLockScreen(args) }
    }

    fun onBiometricError(errorCode: Int) {
        when (errorCode) {
            BiometricPrompt.ERROR_NO_BIOMETRICS ->
                onState { it.copy(shouldShowCheckBoxFingerprint = false) }
        }
    }

    fun onBiometricSuccess() = state.value.run {
        onState { it.copy(isCheckBoxFingerprintChecked = !isCheckBoxFingerprintChecked) }
    }

    fun onLockResult(result: PinResult) {
        val isRegisterType = result is PinResult.Register
        onState { it.pinValidationState(isCheckBoxPinChecked = isRegisterType) }
    }

    fun onScreenShield(isChecked: Boolean) {
        onAction { PreferencesViewAction.ScreenShield(isChecked) }
    }
}
