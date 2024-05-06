package com.fappslab.features.details.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.DecryptSeedPhraseUseCase
import com.fappslab.features.common.domain.usecase.DeleteSeedPhraseUseCase
import com.fappslab.features.common.domain.usecase.EncodeParams
import com.fappslab.features.common.domain.usecase.EncodeSeedPhraseColorUseCase
import com.fappslab.features.common.domain.usecase.SetSeedPhraseUseCase
import com.fappslab.features.common.navigation.DetailsArgs
import com.fappslab.features.details.presentation.model.extension.toDetailsArgs
import com.fappslab.features.details.presentation.model.extension.toSeed
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.seedcake.features.details.R
import com.fappslab.seedcake.libraries.arch.simplepermission.model.PermissionStatus
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import com.fappslab.seedcake.libraries.design.pluto.activity.qrcode.creator.PlutoQrcodeCreator
import com.fappslab.seedcake.libraries.extension.blankString
import com.fappslab.seedcake.libraries.extension.splitToList
import kotlinx.coroutines.launch

internal class DetailsViewModel(
    private val args: DetailsArgs,
    private val setSeedPhraseUseCase: SetSeedPhraseUseCase,
    private val deleteSeedPhraseUseCase: DeleteSeedPhraseUseCase,
    private val decryptSeedPhraseUseCase: DecryptSeedPhraseUseCase,
    private val encodeSeedPhraseColorUseCase: EncodeSeedPhraseColorUseCase
) : ViewModel<DetailsViewState, DetailsViewAction>(DetailsViewState(args)) {

    init {
        createQrcode()
    }

    private fun createQrcode() {
        val qrcode = PlutoQrcodeCreator.create(args.unreadableSeedPhrase)
        onState { it.copy(encryptedSeedBitmap = qrcode) }
    }

    private fun editSeed(seed: Seed) {
        viewModelScope.launch {
            onState { it.copy(shouldShowEditAliasModal = false) }
                .runCatching { setSeedPhraseUseCase(seed) }
                .onFailure { }
                .onSuccess { onState { it.copy(args = seed.toDetailsArgs()) } }
        }
    }

    fun onDeleteConfirmation() {
        viewModelScope.launch {
            onState { it.copy() }
                .runCatching { deleteSeedPhraseUseCase(args.id) }
                .onFailure { }
                .onSuccess { onAction { DetailsViewAction.FinishView } }
        }
    }

    fun onUnlockSeed(passphrase: String) {
        val decryptParams = DecryptParams(args.unreadableSeedPhrase, passphrase)
        viewModelScope.launch {
            onState { it.copy(shouldShowProgressDialog = true) }
                .runCatching {
                    val readableSeedPhrase = decryptSeedPhraseUseCase(decryptParams)
                    val encodeParams = EncodeParams(readableSeedPhrase.splitToList(blankString()))
                    val colorfulSeedPhrase = encodeSeedPhraseColorUseCase(encodeParams)
                    readableSeedPhrase to colorfulSeedPhrase
                }
                .onFailure { unlockSeedFailure(cause = it) }
                .onSuccess { unlockSeedSuccess(resultPair = it) }
                .also { onState { it.copy(shouldShowProgressDialog = false) } }
        }
    }

    private fun unlockSeedFailure(cause: Throwable) {
        val dialogErrorPair = when (cause) {
            is ThrowableValidation -> cause.type.messageRes
            else -> R.string.unknown_error
        } to cause.message
        onState { it.dialogError(dialogErrorPair = dialogErrorPair) }
    }

    private fun unlockSeedSuccess(resultPair: Pair<String, List<Pair<String, String>>>) {
        val (unlockedSeed, coloredSeed) = resultPair
        onAction { DetailsViewAction.UnlockedSeed(UnlockParams(unlockedSeed, coloredSeed)) }
    }

    fun onDeleteVisibility(shouldShow: Boolean = true) {
        onState { it.copy(shouldShowDeleteSeedModal = shouldShow) }
    }

    fun onFullEncryptedSeedVisibility(shouldShow: Boolean = true) {
        onState { it.copy(shouldShowFullEncryptedSeedModal = shouldShow) }
    }

    fun onInfoVisibility(shouldShow: Boolean = true) {
        onState { it.copy(shouldShowInfoModal = shouldShow) }
    }

    fun onEditAliasVisibility(shouldShow: Boolean = true) {
        onState { it.copy(shouldShowEditAliasModal = shouldShow) }
    }

    fun onDecryptErrorVisibility(shouldShow: Boolean) {
        onState { it.copy(shouldShowDecryptErrorModal = shouldShow) }
    }

    fun onSaveToGallery() {
        onInfoVisibility(shouldShow = false)
        onAction { DetailsViewAction.RequestPermission }
    }

    fun onValidationDialog() {
        onAction { DetailsViewAction.Validation }
    }

    fun onSaveToGalleryResult(isSuccess: Boolean) {
        val message = if (isSuccess) {
            R.string.details_save_palette_success_toast
        } else R.string.details_save_palette_failure_toast
        onAction { DetailsViewAction.SaveToGalleryResult(message) }
    }

    fun onCopy() {
        onAction { DetailsViewAction.Copy(args.unreadableSeedPhrase) }
    }

    fun onWhatSeeing() {
        onAction { DetailsViewAction.WhatSeeing }
    }

    fun onEye(isChecked: Boolean = false) {
        val childPosition = if (isChecked) {
            CHILD_SEED
        } else CHILD_LINE
        onState { it.copy(childPosition = childPosition, isEyeChecked = isChecked) }
    }

    fun onCloseSeed() {
        onAction { DetailsViewAction.CloseSeed }
    }

    fun onBack() {
        onAction { DetailsViewAction.FinishView }
    }

    fun onSave(alias: String) = state.value.run {
        val args = args.copy(alias = alias)
        editSeed(args.toSeed())
    }

    fun onPermission(result: PermissionStatus) {
        when (result) {
            PermissionStatus.Denied,
            PermissionStatus.AlwaysDenied -> onDeniedPermissionVisibility()

            PermissionStatus.Granted -> onAction { DetailsViewAction.GrantedPermission }
        }
    }

    fun onDeniedPermissionVisibility(shouldShow: Boolean = true) {
        onState { it.copy(shouldShowDeniedPermissionModal = shouldShow) }
    }

    fun onOpenAppSettings() {
        onDeniedPermissionVisibility(shouldShow = false)
        onAction { DetailsViewAction.OpenAppSettings }
    }
}
