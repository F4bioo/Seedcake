package com.fappslab.features.decrypt.presentation.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fappslab.features.decrypt.presentation.model.PageType
import com.fappslab.seedcake.features.decrypt.R

internal const val FIRST_PAGE = 0
internal const val CHILD_LINE = 0
internal const val CHILD_SEED = 1

internal data class DecryptViewState(
    val encryptedSeed: String? = null,
    val coloredSeed: String? = null,
    val childPosition: Int = CHILD_LINE,
    val pagePosition: Int = FIRST_PAGE,
    val shouldShowDeniedPermissionModal: Boolean = false,
    val shouldShowUnlockSeedErrorModal: Boolean = false,
    val shouldShowProgressDialog: Boolean = false,
    val isEyeChecked: Boolean = false,
    @DrawableRes val endIconRes: Int = R.drawable.plu_ic_qrcode,
    @StringRes val inputSeedErrorRes: Int? = null,
    @StringRes val inputPassErrorRes: Int? = null,
) {

    fun modalError(@StringRes inputSeedErrorRes: Int) = copy(
        inputSeedErrorRes = inputSeedErrorRes,
        shouldShowUnlockSeedErrorModal = true
    )

    fun textChangedUnreadableSeed(
        pageType: PageType,
        unreadableSeed: String
    ): DecryptViewState = when (pageType) {
        PageType.EncryptedSeed -> copy(encryptedSeed = unreadableSeed)
        PageType.ColoredSeed -> copy(coloredSeed = unreadableSeed)
    }.copy(inputSeedErrorRes = null, endIconRes = unreadableSeed.toEndIcon())

    private fun String.toEndIcon(): Int =
        if (isBlank()) {
            R.drawable.plu_ic_qrcode
        } else R.drawable.plu_ic_cancel
}
