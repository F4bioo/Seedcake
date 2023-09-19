package com.fappslab.features.encrypt.result.presentation.extension

import androidx.fragment.app.Fragment
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.libraries.design.pluto.fragment.modal.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.modal.plutoFeedbackModal
import com.fappslab.seedcake.libraries.extension.toHighlightFirstFive

private const val TAG_FULL_ENCRYPTED_SEED_MODAL = "showFullEncryptedSeedModal"

internal fun Fragment.showFullEncryptedSeedModal(
    shouldShow: Boolean,
    encryptedSeed: String,
    closeBlock: (Boolean) -> Unit,
    primaryBlock: () -> Unit
) {
    plutoFeedbackModal {
        titleRes = R.string.encrypted_seed
        messageSpanned = encryptedSeed.toHighlightFirstFive()
        closeButton = { closeBlock(false) }
        primaryButton = {
            buttonTextRes = R.string.common_copy
            buttonAction = primaryBlock
        }
    }.build(shouldShow, childFragmentManager, TAG_FULL_ENCRYPTED_SEED_MODAL)
}
