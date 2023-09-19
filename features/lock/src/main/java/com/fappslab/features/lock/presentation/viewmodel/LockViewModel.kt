package com.fappslab.features.lock.presentation.viewmodel

import com.fappslab.features.common.domain.usecase.DeletePinUseCase
import com.fappslab.features.common.domain.usecase.GetFingerprintStateUseCase
import com.fappslab.features.common.domain.usecase.GetPinUseCase
import com.fappslab.features.common.domain.usecase.SetPinUseCase
import com.fappslab.features.common.navigation.ScreenTypeArgs
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.PinResult
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.ScreenType

internal class LockViewModel(
    private val args: ScreenTypeArgs,
    private val getPinUseCase: GetPinUseCase,
    private val setPinUseCase: SetPinUseCase,
    private val deletePinUseCase: DeletePinUseCase,
    private val getFingerprintStateUseCase: GetFingerprintStateUseCase
) : ViewModel<LockViewState, LockViewAction>(LockViewState(args)) {

    init {
        getSecurityCheckBoxPreference()
    }

    private fun getSecurityCheckBoxPreference() {
        val isCheckBoxFingerprintChecked = getFingerprintStateUseCase()
        onState { it.copy(shouldShowFingerPrintButton = isCheckBoxFingerprintChecked) }
    }

    private fun performActionBasedOnArgs(result: PinResult) {
        val action = when (args) {
            ScreenTypeArgs.LockScreen -> LockViewAction.FinishView
            ScreenTypeArgs.PreferencesRegister -> LockViewAction.Preferences(result)
            ScreenTypeArgs.PreferencesValidate -> {
                deletePinUseCase()
                LockViewAction.Preferences(result)
            }
        }
        onAction { action }
    }


    fun onShowLockScreen() {
        val validateType = getPinUseCase()?.let(ScreenType::Validate)
        val screenType = when (args) {
            ScreenTypeArgs.PreferencesValidate -> {
                onState { it.copy(shouldShowFingerPrintButton = false) }
                validateType
            }

            ScreenTypeArgs.LockScreen -> validateType
            ScreenTypeArgs.PreferencesRegister -> ScreenType.Register
        }
        onState { it.showLockScreen(screenType = screenType) }
    }

    fun onShowBiometric() {
        onAction { LockViewAction.ShowBiometric }
    }

    fun onSuccessPinValidation(result: PinResult) {
        if (result is PinResult.Register) setPinUseCase(result.pin)
        performActionBasedOnArgs(result)
    }

    fun onSuccessBiometricValidation() = state.value.run {
        onAction { LockViewAction.FinishView }
    }

    fun onBackPressed() {
        val action = if (args == ScreenTypeArgs.LockScreen) {
            LockViewAction.FinishApp
        } else LockViewAction.FinishView
        onAction { action }
    }
}
