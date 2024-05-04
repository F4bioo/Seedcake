package com.fappslab.features.encrypt.result.presentation.extension

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fappslab.features.encrypt.result.presentation.ResultFragment
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.GravityType
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.plutoFeedbackDialog
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.modal.plutoFeedbackModal
import com.fappslab.seedcake.libraries.extension.toHighlightBothEndsBeforeMetadata

private const val TAG_FULL_ENCRYPTED_SEED_MODAL = "showFullEncryptedSeedModal"
private const val TAG_WHAT_SEEING_DIALOG = "showWhatSeeingDialog"
private const val TAG_SAVE_ERROR_MODAL = "showSaveErrorModal"

internal fun ResultFragment.showSaveErrorModal(
    shouldShow: Boolean,
    closeBlock: (Boolean) -> Unit,
    primaryBlock: () -> Unit,
    secondaryBlock: () -> Unit
) {
    plutoFeedbackModal {
        titleRes = R.string.common_error_title
        messageRes = R.string.encrypt_save_in_database_error_message
        closeButton = { closeBlock(false) }
        primaryButton = {
            buttonTextRes = R.string.common_try_again
            buttonAction = primaryBlock
        }
        tertiaryButton = {
            buttonTextRes = R.string.common_go_home
            buttonAction = secondaryBlock
        }
    }.build(shouldShow, childFragmentManager, TAG_SAVE_ERROR_MODAL)
}

internal fun Fragment.showFullEncryptedSeedModal(
    shouldShow: Boolean,
    encryptedSeed: String,
    closeBlock: (Boolean) -> Unit,
    primaryBlock: () -> Unit,
    secondaryBlock: () -> Unit
) {

    val message = context?.toHighlightBothEndsBeforeMetadata(encryptedSeed)

    plutoFeedbackModal {
        titleRes = R.string.encrypted_seed
        messageSpanned = message
        closeButton = { closeBlock(false) }
        primaryButton = {
            buttonTextRes = R.string.common_copy
            buttonAction = primaryBlock
        }
        tertiaryButton = {
            buttonTextRes = R.string.common_what_seeing
            buttonAction = secondaryBlock
        }
    }.build(shouldShow, childFragmentManager, TAG_FULL_ENCRYPTED_SEED_MODAL)
}

internal fun Fragment.showWhatSeeingDialog() {
    plutoFeedbackDialog {
        gravityDialog = GravityType.Center
        titleRes = R.string.common_what_seeing
        messageRes = R.string.common_encrypted_seed_explanation
        primaryButton = {
            buttonAction = { dismissAllowingStateLoss() }
        }
    }.build(manager = childFragmentManager, tag = TAG_WHAT_SEEING_DIALOG)
}

internal fun Context.showSaveToast() {
    Toast.makeText(
        this,
        R.string.encrypt_save_in_database_success_message,
        Toast.LENGTH_SHORT
    ).show()
}
