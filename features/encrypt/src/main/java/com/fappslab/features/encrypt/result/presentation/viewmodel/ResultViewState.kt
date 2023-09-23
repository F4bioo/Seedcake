package com.fappslab.features.encrypt.result.presentation.viewmodel

import android.graphics.Bitmap
import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress3
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs

internal data class ResultViewState(
    val args: ResultArgs,
    val progress: Int = Progress3.ordinal,
    val encryptedSeedBitmap: Bitmap? = null,
    val shouldShowError: Boolean = false,
    val shouldEnableSaveButton: Boolean = true,
    val shouldDisableBackButton: Boolean = false,
    val shouldShowFullEncryptedSeedModal: Boolean = false
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
