package com.fappslab.features.encrypt.main.presentation.viewmodel

import androidx.annotation.StringRes

internal data class EncryptViewState(
    val progress: Int = 1,
    val shouldShowErrorDialog: Boolean = false,
    val shouldShowProgressDialog: Boolean = false,
    @StringRes val aliasErrorRes: Int? = null,
    @StringRes val passphrase1ErrorRes: Int? = null,
    @StringRes val passphrase2ErrorRes: Int? = null,
    @StringRes val mnemonicErrorRes: Int? = null,
)
