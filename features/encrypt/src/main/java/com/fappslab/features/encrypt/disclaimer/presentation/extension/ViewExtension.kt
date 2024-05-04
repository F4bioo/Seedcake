package com.fappslab.features.encrypt.disclaimer.presentation.extension

import com.fappslab.features.encrypt.disclaimer.presentation.DisclaimerFragment
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.GravityType
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.plutoFeedbackDialog
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.build

private const val TAG_DISCLAIMER_ERROR_DIALOG = "showDisclaimerErrorDialog"

internal fun DisclaimerFragment.showDisclaimerErrorDialog(
    shouldShow: Boolean,
    primaryBlock: () -> Unit
) {
    plutoFeedbackDialog {
        gravityDialog = GravityType.Center
        titleRes = R.string.encrypt_dialog_title_consent_unconfirmed
        messageRes = R.string.encrypt_dialog_message_consent_unconfirmed
        primaryButton = {
            buttonAction = primaryBlock
        }
    }.build(shouldShow, childFragmentManager, TAG_DISCLAIMER_ERROR_DIALOG)
}
