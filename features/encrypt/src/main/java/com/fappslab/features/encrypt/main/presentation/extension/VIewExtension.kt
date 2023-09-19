package com.fappslab.features.encrypt.main.presentation.extension

import androidx.fragment.app.Fragment
import com.fappslab.features.encrypt.main.presentation.EncryptFragment
import com.fappslab.features.encrypt.main.presentation.model.EncryptParams
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.FragmentEncryptBinding
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.plutoFeedbackDialog
import com.fappslab.seedcake.libraries.design.pluto.fragment.progress.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.progress.plutoProgressDialog
import com.fappslab.seedcake.libraries.extension.isNull

private const val TAG_PASSPHRASE_MISMATCH_ERROR_DIALOG = "showPassphraseMismatchErrorDialog"
private const val TAG_LOADING_DIALOG = "showLoadingDialog"

internal fun EncryptFragment.showPassphraseMismatchErrorDialog(
    shouldShow: Boolean,
    primaryBlock: () -> Unit
) {
    plutoFeedbackDialog {
        titleRes = R.string.passphrase_mismatch_title
        messageRes = R.string.passphrase_mismatch_message
        primaryButton = {
            buttonAction = primaryBlock
        }
    }.build(shouldShow, childFragmentManager, TAG_PASSPHRASE_MISMATCH_ERROR_DIALOG)
}

internal fun FragmentEncryptBinding.getEncryptParams(seed: List<String>): EncryptParams {
    return EncryptParams(
        alias = inputAlias.text.toString().trim(),
        passphrase1 = inputPassphrase1.text.toString(),
        passphrase2 = inputPassphrase2.text.toString(),
        seed = seed
    )
}

internal fun Fragment.showLoadingDialog(shouldShow: Boolean) {
    plutoProgressDialog {
        shouldShowMessage = false
    }.build(shouldShow, childFragmentManager, TAG_LOADING_DIALOG)
}

fun Int?.formValidation(text: String): Int? {
    return if (isNull()) {
        R.string.field_required.takeIf { text.isBlank() }
    } else this
}

fun Int?.formValidation(list: List<String>): Int? {
    return if (isNull()) {
        R.string.field_required.takeIf { list.isEmpty() }
    } else this
}
