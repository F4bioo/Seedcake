package com.fappslab.features.encrypt.secret.presentation.viewmodel.secret

import com.fappslab.features.encrypt.secret.presentation.extension.formValidation
import com.fappslab.features.encrypt.secret.presentation.model.FormParams
import com.fappslab.features.encrypt.secret.presentation.model.SecretArgs
import com.fappslab.libraries.security.model.SecureString
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import com.fappslab.seedcake.libraries.extension.isNull

internal class SecretViewModel : ViewModel<SecretViewState, SecretViewAction>(SecretViewState()) {

    fun onAfterChangePassphrase1(passphrase: String) {
        onState { it.passphrase1ErrorRes(passphrase) }
    }

    fun onAfterChangePassphrase2(passphrase: String) {
        onState { it.passphrase2ErrorRes(passphrase) }
    }

    fun onShowPassphraseGeneratorModal() {
        onAction { SecretViewAction.ShowModal }
    }

    fun onUsePassphrase(securePassphrase: SecureString) {
        onAction { SecretViewAction.Use(securePassphrase) }
    }

    private fun navigateToNext(params: FormParams) {
        val (alias, passphrase) = params
        onAction { SecretViewAction.Result(SecretArgs(alias, passphrase)) }
    }

    fun onNext(params: FormParams) {
        state.value.areInputsPopulated(params) {
            if (params.passphrase1 != params.passphrase2) {
                onState { it.passphraseMismatchErrorRes() }
            } else navigateToNext(params)
        }
    }

    private fun SecretViewState.areInputsPopulated(params: FormParams, block: () -> Unit) {
        val errorResPair = Pair(
            first = passphrase1ErrorRes.formValidation(params.passphrase1),
            second = passphrase2ErrorRes.formValidation(params.passphrase2)
        )
        onState { it.updateInputErrorRes(errorResPair) }

        if (errorResPair.toList().all { it.isNull() }) block()
    }
}
