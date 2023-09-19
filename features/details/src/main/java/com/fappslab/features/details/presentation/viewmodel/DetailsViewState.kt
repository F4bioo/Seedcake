package com.fappslab.features.details.presentation.viewmodel

import android.graphics.Bitmap
import androidx.annotation.StringRes
import com.fappslab.features.common.navigation.DetailsArgs

internal const val CHILD_LINE = 0
internal const val CHILD_SEED = 1

internal data class DetailsViewState(
    val args: DetailsArgs,
    val progress: Int = 2,
    val childPosition: Int = CHILD_LINE,
    val shouldShowError: Boolean = false,
    val shouldShowInfoModal: Boolean = false,
    val shouldEnableSaveButton: Boolean = true,
    val shouldDisableBackButton: Boolean = false,
    val shouldShowEditAliasModal: Boolean = false,
    val shouldShowProgressDialog: Boolean = false,
    val shouldShowDeleteSeedModal: Boolean = false,
    val shouldShowDecryptErrorModal: Boolean = false,
    val shouldShowFullEncryptedSeedModal: Boolean = false,
    val shouldShowDeniedPermissionModal: Boolean = false,
    val encryptedSeedBitmap: Bitmap? = null,
    @StringRes val aliasErrorRes: Int? = null,
    @StringRes val decryptErrorRes: Int? = null
) {

    fun cryptoError(@StringRes decryptErrorRes: Int) = copy(
        decryptErrorRes = decryptErrorRes,
        shouldShowDecryptErrorModal = true
    )
}
