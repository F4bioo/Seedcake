package com.fappslab.features.encrypt.result.presentation.viewmodel

import android.graphics.Bitmap
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs

internal data class ResultViewState(
    val args: ResultArgs,
    val encryptedSeedBitmap: Bitmap? = null,
    val shouldShowError: Boolean = false,
    val shouldEnableSaveButton: Boolean = true,
    val shouldDisableBackButton: Boolean = false,
    val shouldShowFullEncryptedSeedModal: Boolean = false,
    val progress: Int = 2
) {

    fun saveFailure() = copy(
        shouldEnableSaveButton = true,
        shouldDisableBackButton = false
    )

    fun saveStart() = copy(
        shouldEnableSaveButton = false,
        shouldDisableBackButton = true
    )
}
