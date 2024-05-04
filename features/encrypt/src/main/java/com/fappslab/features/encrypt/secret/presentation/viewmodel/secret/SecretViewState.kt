package com.fappslab.features.encrypt.secret.presentation.viewmodel.secret

import androidx.annotation.StringRes
import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress2
import com.fappslab.features.encrypt.secret.presentation.extension.toErrorRes
import com.fappslab.seedcake.features.encrypt.R

internal data class SecretViewState(
    val progress: Int = Progress2.ordinal,
    val shouldShowErrorDialog: Boolean = false,
    @StringRes val passphrase1ErrorRes: Int? = null,
    @StringRes val passphrase2ErrorRes: Int? = null
) {

    fun passphrase1ErrorRes(passphrase: String) = copy(
        passphrase1ErrorRes = passphrase.toErrorRes(),
        passphrase2ErrorRes = null
    )

    fun passphrase2ErrorRes(passphrase: String) = copy(
        passphrase1ErrorRes = null,
        passphrase2ErrorRes = passphrase.toErrorRes()
    )

    fun passphraseMismatchErrorRes() = copy(
        passphrase1ErrorRes = R.string.encrypt_passphrase_mismatch_error,
        passphrase2ErrorRes = R.string.encrypt_passphrase_mismatch_error
    )

    fun updateInputErrorRes(errorResPair: Pair<Int?, Int?>) = copy(
        passphrase1ErrorRes = errorResPair.first,
        passphrase2ErrorRes = errorResPair.second
    )
}
