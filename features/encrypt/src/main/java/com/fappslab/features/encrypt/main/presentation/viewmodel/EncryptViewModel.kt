package com.fappslab.features.encrypt.main.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.fappslab.features.common.domain.usecase.EncryptSeedUseCase
import com.fappslab.features.encrypt.main.presentation.extension.formValidation
import com.fappslab.features.encrypt.main.presentation.model.EncryptParams
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidEntropyLengthException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidMnemonicSeedException
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import com.fappslab.seedcake.libraries.extension.isNull
import com.fappslab.seedcake.libraries.extension.isValidPassphrase
import kotlinx.coroutines.launch

internal class EncryptViewModel(
    private val encryptSeedUseCase: EncryptSeedUseCase,
) : ViewModel<EncryptViewState, EncryptViewAction>(EncryptViewState()) {

    fun onAfterChangeAlias() {
        onState { it.copy(aliasErrorRes = null) }
    }

    fun onAfterChangePassphrase1(passphrase: String) {
        val errorRes = passphrase.takeUnless { it.isEmpty() }
            ?.takeIf { it.isValidPassphrase().not() }
            ?.let { R.string.encrypt_passphrase_strength_requirements }
        onState { it.copy(passphrase1ErrorRes = errorRes) }
    }

    fun onAfterChangePassphrase2(passphrase: String) {
        val errorRes = passphrase.takeUnless { it.isEmpty() }
            ?.takeIf { it.isValidPassphrase().not() }
            ?.let { R.string.encrypt_passphrase_strength_requirements }
        onState { it.copy(passphrase2ErrorRes = errorRes) }
    }

    fun onAfterChangeMnemonic() {
        onState { it.copy(mnemonicErrorRes = null) }
    }

    private fun encrypt(params: EncryptParams) {
        viewModelScope.launch {
            onState { it.copy(shouldShowProgressDialog = true) }
                .runCatching { encryptSeedUseCase(params.seed, params.passphrase1) }
                .onFailure { cryptoError(cause = it) }
                .onSuccess { encryptSuccess(params, encryptedSeed = it) }
                .also { onState { it.copy(shouldShowProgressDialog = false) } }
        }
    }

    private fun cryptoError(cause: Throwable) {
        when (cause) {
            is InvalidMnemonicSeedException ->
                onState { it.copy(mnemonicErrorRes = R.string.invalid_mnemonic_seed_error) }

            is InvalidEntropyLengthException ->
                onState { it.copy(mnemonicErrorRes = R.string.invalid_entropy_length_error) }
        }
    }

    private fun encryptSuccess(params: EncryptParams, encryptedSeed: String) {
        val args = ResultArgs(params.alias, encryptedSeed)
        onAction { EncryptViewAction.Result(args) }
    }

    fun onEncrypt(params: EncryptParams) {
        if (state.value.areInputsPopulated(params)) {
            when (params.passphrase1) {
                params.passphrase2 -> encrypt(params)
                else -> onState { it.copy(shouldShowErrorDialog = true) }
            }
        }
    }

    fun onErrorDismiss() {
        onState { it.copy(shouldShowErrorDialog = false) }
    }

    private fun EncryptViewState.areInputsPopulated(params: EncryptParams): Boolean {
        val aliasErrorRes = aliasErrorRes.formValidation(params.alias)
        onState { it.copy(aliasErrorRes = aliasErrorRes) }
        val passphrase1ErrorRes = passphrase1ErrorRes.formValidation(params.passphrase1)
        onState { it.copy(passphrase1ErrorRes = passphrase1ErrorRes) }
        val passphrase2ErrorRes = passphrase2ErrorRes.formValidation(params.passphrase2)
        onState { it.copy(passphrase2ErrorRes = passphrase2ErrorRes) }
        val mnemonicErrorRes = mnemonicErrorRes.formValidation(params.seed)
        onState { it.copy(mnemonicErrorRes = mnemonicErrorRes) }

        return listOf(
            aliasErrorRes,
            passphrase1ErrorRes,
            passphrase2ErrorRes,
            mnemonicErrorRes
        ).all { it.isNull() }
    }
}
