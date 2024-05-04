package com.fappslab.features.encrypt.seed.presentation.viewmodel.seed

import androidx.lifecycle.viewModelScope
import com.fappslab.features.common.domain.usecase.EncryptSeedPhraseUseCase
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs
import com.fappslab.features.encrypt.secret.presentation.model.SecretArgs
import com.fappslab.features.encrypt.seed.presentation.component.InputType
import com.fappslab.features.encrypt.seed.presentation.extension.prepareToEncrypt
import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmType
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import kotlinx.coroutines.launch

internal class SeedViewModel(
    private val encryptSeedPhraseUseCase: EncryptSeedPhraseUseCase,
) : ViewModel<SeedViewState, SeedViewAction>(SeedViewState()) {

    fun onEncrypt(secret: SecretArgs) {
        val params = state.value.prepareToEncrypt(secret)
        viewModelScope.launch {
            onState { it.copy(shouldShowProgressDialog = true) }
                .runCatching { encryptSeedPhraseUseCase(params) }
                .onFailure { cryptoFailure(cause = it) }
                .onSuccess { encryptSuccess(secret.alias, encryptedSeed = it) }
                .also { onState { it.copy(shouldShowProgressDialog = false) } }
        }
    }

    private fun cryptoFailure(cause: Throwable) {
        val dialogErrorPair = when (cause) {
            is ThrowableValidation -> cause.type.messageRes
            else -> R.string.encryption_failed_error
        } to cause.message
        onState { it.dialogError(dialogErrorPair) }
    }

    private fun encryptSuccess(alias: String, encryptedSeed: String) {
        val args = ResultArgs(alias, encryptedSeed)
        onState { it.wordChanged(emptyList()) }
        onAction { SeedViewAction.Result(args) }
    }

    fun onLockSeedErrorVisibilityDialog(shouldShow: Boolean = false) {
        onState { it.copy(shouldShowLockSeedErrorDialog = shouldShow) }
    }

    fun onAlgorithmVisibilityPage(shouldShow: Boolean = true) {
        onState { it.copy(shouldShowAlgorithmPage = shouldShow) }
    }

    fun onAlgorithmClicked(algorithmType: AlgorithmType) {
        onState { it.copy(algorithmType = algorithmType, shouldShowAlgorithmPage = false) }
    }

    fun onCheckInputType(radioIdRes: Int) {
        val inputType = if (radioIdRes == InputType.TWELVE.radioIdRes)
            InputType.TWELVE else InputType.TWENTY_FOUR
        onState { it.copy(inputType = inputType) }
    }

    fun onExpandedFields() {
        onState { it.copy(inputType = InputType.TWENTY_FOUR) }
    }

    fun onSuggestionClicked(suggestion: String) {
        onAction { SeedViewAction.Suggestion(suggestion) }
    }

    fun onBackspaceKey() {
        onAction { SeedViewAction.BackspaceKey }
    }

    fun onLetterKey(letter: String) {
        onAction { SeedViewAction.LetterKey(letter) }
    }

    fun onNextInput() {
        onAction { SeedViewAction.NextInput }
    }

    fun onWordsChanged(inputWords: List<String>) {
        onState { it.wordChanged(inputWords) }
    }
}
