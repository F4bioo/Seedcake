package com.fappslab.features.details.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.domain.usecase.DecryptSeedUseCase
import com.fappslab.features.common.domain.usecase.DeleteSeedUseCase
import com.fappslab.features.common.domain.usecase.EncodeSeedColorUseCase
import com.fappslab.features.common.domain.usecase.SetSeedUseCase
import com.fappslab.features.common.navigation.DetailsArgs
import com.fappslab.features.details.presentation.extensions.formValidation
import com.fappslab.features.details.presentation.model.extension.toDetailsArgs
import com.fappslab.features.details.presentation.model.extension.toSeed
import com.fappslab.seedcake.features.details.R
import com.fappslab.seedcake.libraries.arch.exceptions.BlankPassphraseException
import com.fappslab.seedcake.libraries.arch.exceptions.DecryptionFailedException
import com.fappslab.seedcake.libraries.arch.exceptions.DecryptionTimeoutException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidPassphraseException
import com.fappslab.seedcake.libraries.arch.simplepermission.model.PermissionStatus
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import com.fappslab.seedcake.libraries.design.pluto.activity.qrcode.creator.PlutoQrcodeCreator
import kotlinx.coroutines.launch

internal class DetailsViewModel(
    private val args: DetailsArgs,
    private val setSeedUseCase: SetSeedUseCase,
    private val deleteSeedUseCase: DeleteSeedUseCase,
    private val decryptSeedUseCase: DecryptSeedUseCase,
    private val encodeSeedColorUseCase: EncodeSeedColorUseCase
) : ViewModel<DetailsViewState, DetailsViewAction>(DetailsViewState(args)) {

    init {
        createQrcode()
    }

    private fun createQrcode() {
        val qrcode = PlutoQrcodeCreator.create(args.encryptedSeed)
        onState { it.copy(encryptedSeedBitmap = qrcode) }
    }

    private fun editSeed(seed: Seed) {
        viewModelScope.launch {
            onState { it.copy(shouldShowEditAliasModal = false) }
                .runCatching { setSeedUseCase(seed) }
                .onFailure { }
                .onSuccess { onState { it.copy(args = seed.toDetailsArgs()) } }
        }
    }

    private fun DetailsViewState.isInputPopulated(alias: String): Boolean {
        val aliasErrorRes = aliasErrorRes.formValidation(alias)
        onState { it.copy(aliasErrorRes = aliasErrorRes) }

        return alias.isNotBlank()
    }

    fun onDeleteConfirmation() {
        viewModelScope.launch {
            onState { it.copy() }
                .runCatching { deleteSeedUseCase(args.id) }
                .onFailure { }
                .onSuccess { onAction { DetailsViewAction.FinishView } }
        }
    }

    fun onDecryptSeed(passphrase: String) {
        val encryptedSeed = args.encryptedSeed
        viewModelScope.launch {
            onState { it.copy(shouldShowProgressDialog = true) }
                .runCatching {
                    val decryptedSeed = decryptSeedUseCase(encryptedSeed, passphrase)
                    val coloredSeed = encodeSeedColorUseCase(decryptedSeed)
                    decryptedSeed to coloredSeed
                }
                .onFailure { cryptoError(cause = it) }
                .onSuccess { decryptSuccess(resultPair = it) }
                .also { onState { it.copy(shouldShowProgressDialog = false) } }
        }
    }

    private fun cryptoError(cause: Throwable) {
        val decryptErrorRes = when (cause) {
            // Encryptor
            is BlankPassphraseException -> R.string.blank_passphrase_error
            is InvalidPassphraseException -> R.string.invalid_passphrase_error
            is DecryptionFailedException -> R.string.decryption_failed_error
            is DecryptionTimeoutException -> R.string.decryption_timeout_error
            // Others
            else -> R.string.unexpected_crypto_error
        }
        onState { it.cryptoError(decryptErrorRes = decryptErrorRes) }
    }

    private fun decryptSuccess(resultPair: Pair<String, List<Pair<String, String>>>) {
        val (decryptedSeed, coloredSeed) = resultPair
        onAction { DetailsViewAction.Decrypted(DecryptParams(decryptedSeed, coloredSeed)) }
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
        onState { it.copy(shouldShowEditAliasModal = shouldShow, aliasErrorRes = null) }
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
            R.string.save_palette_success_toast
        } else R.string.save_palette_failure_toast
        onAction { DetailsViewAction.SaveToGalleryResult(message) }
    }

    fun onCopy() {
        onAction { DetailsViewAction.Copy(args.encryptedSeed) }
    }

    fun onEye(isChecked: Boolean) {
        val childPosition = if (isChecked) {
            CHILD_SEED
        } else CHILD_LINE
        onState { it.copy(childPosition = childPosition) }
    }

    fun onClose() {
        onAction { DetailsViewAction.FinishView }
    }

    fun onSave(alias: String) = state.value.run {
        if (isInputPopulated(alias)) {
            val args = args.copy(alias = alias)
            editSeed(args.toSeed())
        }
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
