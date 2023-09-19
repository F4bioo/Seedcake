package com.fappslab.features.encrypt.disclaimer.presentation.extension

import com.fappslab.features.encrypt.disclaimer.presentation.DisclaimerFragment
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.plutoFeedbackDialog

private const val TAG_DISCLAIMER_ERROR_DIALOG = "showDisclaimerErrorDialog"

internal fun DisclaimerFragment.showDisclaimerErrorDialog(
    shouldShow: Boolean,
    primaryBlock: () -> Unit
) {
    plutoFeedbackDialog {
        titleRes = R.string.dialog_title_consent_unconfirmed
        messageRes = R.string.dialog_message_consent_unconfirmed
        primaryButton = {
            buttonAction = primaryBlock
        }
    }.build(shouldShow, childFragmentManager, TAG_DISCLAIMER_ERROR_DIALOG)
}
