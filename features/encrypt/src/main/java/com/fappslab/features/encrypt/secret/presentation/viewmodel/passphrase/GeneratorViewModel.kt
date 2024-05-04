package com.fappslab.features.encrypt.secret.presentation.viewmodel.passphrase

import com.fappslab.features.encrypt.secret.domain.usecase.MAX_PASSPHRASE_LENGTH
import com.fappslab.features.encrypt.secret.domain.usecase.MIN_PASSPHRASE_LENGTH
import com.fappslab.features.encrypt.secret.domain.usecase.PassphraseGeneratorUseCase
import com.fappslab.libraries.security.model.SecureString
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel

internal class GeneratorViewModel(
    private val passphraseGeneratorUseCase: PassphraseGeneratorUseCase
) : ViewModel<GeneratorViewState, GeneratorViewAction>(GeneratorViewState()) {

    fun onGeneratePassphrase(length: Int) {
        runCatching { passphraseGeneratorUseCase(length) }
            .onFailure { it.printStackTrace() }
            .onSuccess { onAction { GeneratorViewAction.Generated(it) } }
    }

    fun onUse(securePassphrase: SecureString) {
        onAction { GeneratorViewAction.UseButton(securePassphrase) }
    }

    fun onClose() {
        onAction { GeneratorViewAction.CloseButton }
    }

    fun onCopyToClipboard(securePassphrase: SecureString) {
        onAction { GeneratorViewAction.Copy(securePassphrase) }
    }

    fun onWarningCopy(securePassphrase: SecureString) {
        onAction { GeneratorViewAction.WarningCopy(securePassphrase) }
    }

    fun onRandomGenerate(sliderSizeValue: Int) {
        onGeneratePassphrase(sliderSizeValue)
        onAction { GeneratorViewAction.RandomButton }
    }

    fun onDecreaseLength(sliderSizeValue: Int) {
        val length = getSliderSizeLength(sliderSizeValue.dec())
        if (length < MIN_PASSPHRASE_LENGTH) return
        updateSliderSizeValue(length)
    }

    fun onIncreaseLength(sliderSizeValue: Int) {
        val length = getSliderSizeLength(sliderSizeValue.inc())
        if (length > MAX_PASSPHRASE_LENGTH) return
        updateSliderSizeValue(length)
    }

    private fun getSliderSizeLength(sliderSizeValue: Int): Int {
        return sliderSizeValue.coerceIn(MIN_PASSPHRASE_LENGTH, MAX_PASSPHRASE_LENGTH)
    }

    private fun updateSliderSizeValue(sliderSizeValue: Int) {
        onState { it.copy(sliderSizeValue = sliderSizeValue) }
    }
}
