package com.fappslab.features.encrypt.disclaimer.presentation.viewmodel

import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress1

internal data class DisclaimerViewState(
    val progress: Int = Progress1.ordinal,
    val isConfirmChecked: Boolean = false,
    val shouldShowError: Boolean = false
)
