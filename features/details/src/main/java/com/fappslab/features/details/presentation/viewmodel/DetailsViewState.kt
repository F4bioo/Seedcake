package com.fappslab.features.details.presentation.viewmodel

import android.graphics.Bitmap
import com.fappslab.features.common.navigation.DetailsArgs
import com.fappslab.seedcake.features.details.R

internal const val CHILD_LINE = 0
internal const val CHILD_SEED = 1

internal data class DetailsViewState(
    val args: DetailsArgs,
    val progress: Int = 2,
    val childPosition: Int = CHILD_LINE,
    val isEyeChecked: Boolean = false,
    val shouldShowInfoModal: Boolean = false,
    val shouldEnableSaveButton: Boolean = true,
    val shouldDisableBackButton: Boolean = false,
    val shouldShowEditAliasModal: Boolean = false,
    val shouldShowProgressDialog: Boolean = false,
    val shouldShowDeleteSeedModal: Boolean = false,
    val shouldShowDecryptErrorModal: Boolean = false,
    val shouldShowFullEncryptedSeedModal: Boolean = false,
    val shouldShowDeniedPermissionModal: Boolean = false,
    val dialogErrorPair: Pair<Int, String?> = Pair(R.string.unknown_error, null),
    val encryptedSeedBitmap: Bitmap? = null
) {

    fun dialogError(dialogErrorPair: Pair<Int, String?>) = copy(
        shouldShowDecryptErrorModal = true,
        dialogErrorPair = dialogErrorPair
    )
}
