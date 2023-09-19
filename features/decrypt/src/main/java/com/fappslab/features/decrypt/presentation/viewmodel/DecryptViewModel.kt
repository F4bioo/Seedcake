package com.fappslab.features.decrypt.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.fappslab.features.common.domain.usecase.DecodeSeedColorUseCase
import com.fappslab.features.common.domain.usecase.DecryptSeedUseCase
import com.fappslab.features.decrypt.presentation.model.PageType
import com.fappslab.seedcake.features.decrypt.R
import com.fappslab.seedcake.libraries.arch.exceptions.BlankColoredSeedException
import com.fappslab.seedcake.libraries.arch.exceptions.BlankEncryptedSeedException
import com.fappslab.seedcake.libraries.arch.exceptions.BlankPassphraseException
import com.fappslab.seedcake.libraries.arch.exceptions.DecryptionFailedException
import com.fappslab.seedcake.libraries.arch.exceptions.DecryptionTimeoutException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidColorException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidColorFormatException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidEncryptedSeedException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidEntropyLengthException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidPassphraseException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidWordException
import com.fappslab.seedcake.libraries.arch.simplepermission.model.PermissionStatus
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import kotlinx.coroutines.launch

internal class DecryptViewModel(
    private val pageType: PageType,
    private val decryptSeedUseCase: DecryptSeedUseCase,
    private val decodeSeedColorUseCase: DecodeSeedColorUseCase
) : ViewModel<DecryptViewState, DecryptViewAction>(DecryptViewState()) {

    fun onDecryptSeed(passphrase: String) {
        val encryptedSeed = state.value.encryptedSeed.orEmpty()
        viewModelScope.launch {
            onState { it.copy(shouldShowProgressDialog = true) }.runCatching {
                decryptSeedUseCase(
                    encryptedSeed, passphrase
                )
            }.onFailure { unlockSeedFailure(cause = it) }
                .onSuccess { onAction { DecryptViewAction.UnlockedSeed(readableSeed = it) } }
                .also { onState { it.copy(shouldShowProgressDialog = false) } }
        }
    }

    private fun decodeSeedColor() {
        val coloredSeed = state.value.coloredSeed.orEmpty()
        viewModelScope.launch {
            onState { it.copy(shouldShowProgressDialog = true) }
                .runCatching { decodeSeedColorUseCase(coloredSeed) }
                .onFailure { unlockSeedFailure(cause = it) }
                .onSuccess { onAction { DecryptViewAction.UnlockedSeed(readableSeed = it) } }
                .also { onState { it.copy(shouldShowProgressDialog = false) } }
        }
    }

    private fun unlockSeedFailure(cause: Throwable) {
        when (cause) {
            is BlankPassphraseException,
            is InvalidPassphraseException -> notifyPassphraseError(cause)

            else -> notifyInputSeedError(cause)
        }
    }

    private fun notifyPassphraseError(cause: Throwable) {
        val inputSeedErrorRes = when (cause) {
            is BlankPassphraseException -> R.string.blank_passphrase_error
            is InvalidPassphraseException -> R.string.invalid_passphrase_error
            else -> R.string.unexpected_crypto_error
        }
        onState { it.modalError(inputSeedErrorRes = inputSeedErrorRes) }
    }

    private fun notifyInputSeedError(cause: Throwable) {
        val inputPassErrorRes = when (cause) {
            // Encryptor
            is BlankEncryptedSeedException -> R.string.blank_encrypted_seed_error
            is InvalidEncryptedSeedException -> R.string.invalid_encrypted_seed_error
            is DecryptionFailedException -> R.string.decryption_failed_error
            is DecryptionTimeoutException -> R.string.decryption_timeout_error
            // Bib39Colors
            is BlankColoredSeedException -> R.string.blank_colored_seed_error
            is InvalidWordException -> R.string.invalid_word_bip39_error
            is InvalidColorFormatException -> R.string.invalid_color_format_error
            is InvalidColorException -> R.string.invalid_bip39_colors_error
            // Others
            is InvalidEntropyLengthException -> R.string.invalid_entropy_length_error
            else -> R.string.unexpected_crypto_error
        }
        onState { it.copy(inputSeedErrorRes = inputPassErrorRes) }
    }

    fun onTextChangedUnreadableSeed(unreadableSeed: String) {
        onState { it.textChangedUnreadableSeed(pageType, unreadableSeed) }
    }

    fun onPermission(result: PermissionStatus) {
        when (result) {
            PermissionStatus.Denied,
            PermissionStatus.AlwaysDenied -> onDeniedPermissionVisibility()

            PermissionStatus.Granted -> onAction { DecryptViewAction.GrantedPermission }
        }
    }

    fun onScannerResult(encryptedSeed: String) {
        onAction { DecryptViewAction.ScannerResult(encryptedSeed) }
    }

    fun onDeniedPermissionVisibility(shouldShow: Boolean = true) {
        onState { it.copy(shouldShowDeniedPermissionModal = shouldShow) }
    }

    fun onUnlockSeedErrorVisibility(shouldShow: Boolean) {
        onState { it.copy(shouldShowUnlockSeedErrorModal = shouldShow) }
    }

    fun onUnlockSeed() {
        when (pageType) {
            PageType.EncryptedSeed -> onAction { DecryptViewAction.Validation }
            PageType.ColoredSeed -> decodeSeedColor()
        }
    }

    fun onOpenAppSettings() {
        onDeniedPermissionVisibility(shouldShow = false)
        onAction { DecryptViewAction.OpenAppSettings }
    }

    fun onEye(isChecked: Boolean = false) {
        val childPosition = if (isChecked) {
            CHILD_SEED
        } else CHILD_LINE
        onState { it.copy(childPosition = childPosition, isEyeChecked = isChecked) }
    }

    fun onClear() {
        onAction { DecryptViewAction.ClearInput }
    }

    fun onEndIcon() = state.value.run {
        val isScannerAction = when (pageType) {
            PageType.EncryptedSeed -> encryptedSeed.isNullOrEmpty()
            PageType.ColoredSeed -> coloredSeed.isNullOrEmpty()
        }
        if (isScannerAction) {
            onAction { DecryptViewAction.RequestPermission }
        } else onClear()
    }
}
