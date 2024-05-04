package com.fappslab.features.preferences.domain.model

internal data class CheckBoxPreference(
    val isCheckBoxPinChecked: Boolean,
    val isCheckBoxShufflePinChecked: Boolean,
    val isCheckBoxFingerprintChecked: Boolean
)
