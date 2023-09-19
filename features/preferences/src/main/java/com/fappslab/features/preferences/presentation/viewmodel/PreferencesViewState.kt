package com.fappslab.features.preferences.presentation.viewmodel

internal data class PreferencesViewState(
    val isPinValidated: Boolean = false,
    val isCheckBoxPinChecked: Boolean = false,
    val isCheckBoxFingerprintChecked: Boolean = false,
    val shouldShowCheckBoxFingerprint: Boolean = false
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
        isCheckBoxPinChecked: Boolean,
        isCheckBoxFingerprintChecked: Boolean
    ): PreferencesViewState {
        return copy(
            isCheckBoxPinChecked = isCheckBoxPinChecked,
            isCheckBoxFingerprintChecked = isCheckBoxFingerprintChecked
        )
    }
}
