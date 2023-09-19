package com.fappslab.features.encrypt.disclaimer.presentation.viewmodel

internal data class DisclaimerViewState(
    val isConfirmChecked: Boolean = false,
    val shouldShowError: Boolean = false,
    val progress: Int = 0
)
