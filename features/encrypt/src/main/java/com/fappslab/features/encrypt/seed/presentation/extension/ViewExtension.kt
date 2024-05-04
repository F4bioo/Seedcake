package com.fappslab.features.encrypt.seed.presentation.extension

import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fappslab.features.common.domain.usecase.EncryptParams
import com.fappslab.features.encrypt.secret.presentation.model.SecretArgs
import com.fappslab.features.encrypt.seed.presentation.AlgorithmFragment
import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmType
import com.fappslab.features.encrypt.seed.presentation.viewmodel.seed.SeedViewState
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.GravityType
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.plutoFeedbackDialog
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.progress.plutoProgressDialog
import com.fappslab.seedcake.libraries.extension.filterNotBlank
import com.fappslab.seedcake.libraries.extension.getDrawableRes

private const val TAG_LOADING_DIALOG = "showLoadingDialog"
private const val TAG_ENCRYPT_FAILURE_DIALOG = "showLockSeedErrorDialog"
private const val TAG_ALGORITHM_PAGE = "showAlgorithmPage"

internal fun SeedViewState.prepareToEncrypt(args: SecretArgs): EncryptParams {
    return EncryptParams(
        passphrase = args.passphrase,
        cipherSpec = algorithmType.cipherSpec,
        readableSeedPhrase = inputWords.filterNotBlank()
    )
}

internal fun Fragment.showLoadingDialog(shouldShow: Boolean) {
    plutoProgressDialog {
        shouldShowMessage = false
    }.build(shouldShow, childFragmentManager, TAG_LOADING_DIALOG)
}

internal fun Fragment.showLockSeedErrorDialog(
    dialogErrorPair: Pair<Int, String?>,
    shouldShow: Boolean,
    primaryBlock: (Boolean) -> Unit
) {
    val (messageRes, placeholder) = dialogErrorPair
    val message = getString(messageRes, placeholder)
    plutoFeedbackDialog {
        gravityDialog = GravityType.Center
        titleRes = R.string.common_error_title
        messageText = message
        primaryButton = {
            buttonAction = { primaryBlock(false) }
        }
    }.build(shouldShow, childFragmentManager, TAG_ENCRYPT_FAILURE_DIALOG)
}

internal fun Fragment.showAlgorithmPage(
    shouldShow: Boolean,
    closeBlock: (Boolean) -> Unit,
    primaryBlock: (AlgorithmType) -> Unit
) {
    AlgorithmFragment.createFragment {
        onItemClicked = primaryBlock
        onCloseClicked = { closeBlock(false) }
    }.build(shouldShow, childFragmentManager, TAG_ALGORITHM_PAGE)
}

internal fun TextView.setTextTitle(type: AlgorithmType) {
    val title = context.getString(type.titleRes)
    text = if (type.isRecommended) {
        context.getString(R.string.encrypt_algorithm_recommended, title)
    } else title
}

internal fun TextView.setDrawableEnd(drawableResId: Int) {
    val drawable = context.getDrawableRes(drawableResId)
    setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
}
