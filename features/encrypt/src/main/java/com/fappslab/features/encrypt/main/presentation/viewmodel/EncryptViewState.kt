package com.fappslab.features.encrypt.main.presentation.viewmodel

import androidx.annotation.StringRes
import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress2

internal data class EncryptViewState(
    val progress: Int = Progress2.ordinal,
    val shouldShowErrorDialog: Boolean = false,
    val shouldShowProgressDialog: Boolean = false,
    @StringRes val aliasErrorRes: Int? = null,
    @StringRes val passphrase1ErrorRes: Int? = null,
    @StringRes val passphrase2ErrorRes: Int? = null,
    @StringRes val mnemonicErrorRes: Int? = null,
)
