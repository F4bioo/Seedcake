package com.fappslab.features.preferences.presentation.viewmodel

import com.fappslab.features.preferences.domain.model.CheckBoxPreference

internal data class PreferencesViewState(
    val isPinValidated: Boolean = false,
    val isCheckBoxPinChecked: Boolean = false,
    val isCheckBoxFingerprintChecked: Boolean = false,
    val shouldShowCheckBoxFingerprint: Boolean = false,
    val isCheckBoxShufflePinChecked: Boolean = true
) {

    fun pinValidationState(
        isCheckBoxPinChecked: Boolean
    ): PreferencesViewState {
        return copy(
            isCheckBoxPinChecked = isCheckBoxPinChecked,
            isCheckBoxFingerprintChecked = false
        )
    }

    fun securityCheckBoxState(
        checkBoxPreference: CheckBoxPreference
    ): PreferencesViewState {
        return copy(
            isCheckBoxPinChecked = checkBoxPreference.isCheckBoxPinChecked,
            isCheckBoxShufflePinChecked = checkBoxPreference.isCheckBoxShufflePinChecked,
            isCheckBoxFingerprintChecked = checkBoxPreference.isCheckBoxFingerprintChecked
        )
    }
}
