package com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview

import androidx.annotation.StringRes

internal data class ValidationResult(
    val isValid: Boolean,
    @StringRes val errorMessageRes: Int?
)
