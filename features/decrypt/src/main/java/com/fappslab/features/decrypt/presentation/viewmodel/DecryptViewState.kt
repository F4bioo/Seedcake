package com.fappslab.features.decrypt.presentation.viewmodel

import androidx.annotation.DrawableRes
import com.fappslab.features.decrypt.presentation.model.PageType
import com.fappslab.seedcake.features.decrypt.R

internal const val FIRST_PAGE = 0
internal const val CHILD_LINE = 0
internal const val CHILD_SEED = 1

internal data class DecryptViewState(
    val unreadableSeedPhrase: String? = null,
    val coloredSeed: String? = null,
    val isEyeChecked: Boolean = false,
    val childPosition: Int = CHILD_LINE,
    val pagePosition: Int = FIRST_PAGE,
    val shouldShowDeniedPermissionModal: Boolean = false,
    val shouldShowUnlockSeedErrorModal: Boolean = false,
    val shouldShowProgressDialog: Boolean = false,
    val inputErrorPair: Pair<Int, String?>? = null,
    val dialogErrorPair: Pair<Int, String?> = Pair(R.string.unknown_error, null),
    @DrawableRes val endIconRes: Int = R.drawable.plu_ic_qrcode
) {

    fun dialogError(dialogErrorPair: Pair<Int, String?>) = copy(
        shouldShowUnlockSeedErrorModal = true,
        dialogErrorPair = dialogErrorPair
    )

    fun textChangedUnreadableSeed(
        pageType: PageType,
        unreadableSeed: String
    ): DecryptViewState = when (pageType) {
        PageType.EncryptedSeed -> copy(unreadableSeedPhrase = unreadableSeed)
        PageType.ColoredSeed -> copy(coloredSeed = unreadableSeed)
    }.copy(inputErrorPair = null, endIconRes = unreadableSeed.toEndIcon(), isEyeChecked = false)

    private fun String.toEndIcon(): Int =
        if (isBlank()) {
            R.drawable.plu_ic_qrcode
        } else R.drawable.plu_ic_cancel
}
