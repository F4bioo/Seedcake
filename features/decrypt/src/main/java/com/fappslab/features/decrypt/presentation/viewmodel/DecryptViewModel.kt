package com.fappslab.features.decrypt.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.fappslab.features.common.domain.usecase.DecodeSeedPhraseColorUseCase
import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.DecryptSeedPhraseUseCase
import com.fappslab.features.decrypt.presentation.model.PageType
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType
import com.fappslab.seedcake.features.decrypt.R
import com.fappslab.seedcake.libraries.arch.simplepermission.model.PermissionStatus
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import kotlinx.coroutines.launch

internal class DecryptViewModel(
    private val pageType: PageType,
    private val decryptSeedPhraseUseCase: DecryptSeedPhraseUseCase,
    private val decodeSeedPhraseColorUseCase: DecodeSeedPhraseColorUseCase
) : ViewModel<DecryptViewState, DecryptViewAction>(DecryptViewState()) {

    fun onUnlockSeed(passphrase: String) {
        val params = DecryptParams(state.value.unreadableSeedPhrase.orEmpty(), passphrase)
        viewModelScope.launch {
            onState { it.copy(shouldShowProgressDialog = true) }
                .runCatching { decryptSeedPhraseUseCase(params) }
                .onFailure { unlockSeedFailure(cause = it) }
                .onSuccess { onAction { DecryptViewAction.UnlockedSeed(readableSeed = it) } }
                .also { onState { it.copy(shouldShowProgressDialog = false) } }
        }
    }

    private fun decodeSeedColor() {
        val coloredSeed = state.value.coloredSeed
        viewModelScope.launch {
            onState { it.copy(shouldShowProgressDialog = true) }
                .runCatching { decodeSeedPhraseColorUseCase(coloredSeed.orEmpty()) }
                .onFailure { unlockSeedFailure(cause = it) }
                .onSuccess { onAction { DecryptViewAction.UnlockedSeed(readableSeed = it) } }
                .also { onState { it.copy(shouldShowProgressDialog = false) } }
        }
    }

    private fun unlockSeedFailure(cause: Throwable) {
        when (cause) {
            is ThrowableValidation -> when (cause.type) {
                ValidationType.PASSPHRASE_EMPTY,
                ValidationType.PASSPHRASE_INVALID -> notifyPassphraseError(cause)

                else -> notifyInputSeedError(cause)
            }
        }
    }

    private fun notifyPassphraseError(cause: Throwable) {
        val dialogErrorPair = (cause as ThrowableValidation)
            .type.messageRes to cause.message
        onState { it.dialogError(dialogErrorPair) }
    }

    private fun notifyInputSeedError(cause: Throwable) {
        val errorPair = when (cause) {
            is ThrowableValidation -> cause.type.messageRes
            else -> R.string.unknown_error
        } to cause.message
        onState { it.copy(inputErrorPair = errorPair) }
    }

    fun onTextChangedUnreadableSeed(unreadableSeed: String) {
        if (unreadableSeed.isEmpty()) onClear()
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

    fun onUnlockSeedErrorVisibilityDialog(shouldShow: Boolean) {
        onState { it.copy(shouldShowUnlockSeedErrorModal = shouldShow) }
    }

    fun onUnlockSeed() {
        when (pageType) {
            PageType.EncryptedSeed -> decryptSeedOrNotify()
            PageType.ColoredSeed -> decodeSeedColor()
        }
    }

    private fun decryptSeedOrNotify() = state.value.run {
        if (unreadableSeedPhrase.isNullOrEmpty()) {
            onState { it.copy(inputErrorPair = R.string.encryption_phrase_empty to null) }
        } else onAction { DecryptViewAction.Validation }
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
            PageType.EncryptedSeed -> unreadableSeedPhrase.isNullOrEmpty()
            PageType.ColoredSeed -> coloredSeed.isNullOrEmpty()
        }
        if (isScannerAction) {
            onAction { DecryptViewAction.RequestPermission }
        } else onClear()
    }
}
