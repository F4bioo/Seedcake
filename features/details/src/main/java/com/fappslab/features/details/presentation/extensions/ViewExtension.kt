package com.fappslab.features.details.presentation.extensions

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.fappslab.seedcake.features.details.R
import com.fappslab.seedcake.features.details.databinding.DetailsIncludeSeedBinding
import com.fappslab.seedcake.features.details.databinding.DetailsModalEditBinding
import com.fappslab.seedcake.features.details.databinding.DetailsModalInfoBinding
import com.fappslab.seedcake.libraries.arch.simplepermission.extension.permissionLauncher
import com.fappslab.seedcake.libraries.arch.simplepermission.launcher.PermissionLauncher
import com.fappslab.seedcake.libraries.arch.simplepermission.model.PermissionStatus
import com.fappslab.seedcake.libraries.design.pluto.activity.qrcode.creator.PlutoQrcodeCreator
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.GravityType
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.plutoFeedbackDialog
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.modal.plutoFeedbackModal
import com.fappslab.seedcake.libraries.design.pluto.fragment.progress.plutoProgressDialog
import com.fappslab.seedcake.libraries.extension.getTextOrHint
import com.fappslab.seedcake.libraries.extension.image.saveToGallery
import com.fappslab.seedcake.libraries.extension.isNull
import com.fappslab.seedcake.libraries.extension.showNumberedSeed
import com.fappslab.seedcake.libraries.extension.toHighlightBothEndsBeforeMetadata

private const val DELIMITERS = " "
private const val FILE_NAME = "color_palette"
private const val TAG_FULL_ENCRYPTED_SEED_MODAL = "showFullEncryptedSeedModal"
private const val TAG_WHAT_SEEING_DIALOG = "showWhatSeeingDialog"
private const val TAG_INFO_MODAL = "showInfoModal"
private const val TAG_DELETE_SEED_MODAL = "showDeleteSeedModal"
private const val TAG_EDIT_ALIAS_MODAL = "showEditAliasModal"
private const val TAG_DECRYPT_ERROR_DIALOG = "showUnlockSeedErrorDialog"
private const val TAG_LOADING_DIALOG = "showLoadingDialog"
private const val TAG_DENIED_STORAGE_PERMISSION_MODAL = "showDeniedStoragePermissionModal"

internal fun Fragment.showFullEncryptedSeedModal(
    shouldShow: Boolean,
    encryptedSeed: String,
    closeBlock: (Boolean) -> Unit,
    primaryBlock: () -> Unit,
    secondaryBlock: () -> Unit,
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

internal fun Fragment.showInfoModal(
    shouldShow: Boolean,
    closeBlock: (Boolean) -> Unit,
    primaryBlock: () -> Unit
) {
    val binding = DetailsModalInfoBinding.inflate(layoutInflater)

    plutoFeedbackModal {
        customView = binding.root
        closeButton = { closeBlock(false) }
        primaryButton = {
            buttonTextRes = R.string.details_colored_seed_security_save
            buttonAction = {
                primaryBlock()
                dismissAllowingStateLoss()
            }
        }
    }.build(shouldShow, childFragmentManager, TAG_INFO_MODAL)
}

internal fun Fragment.showDeleteSeedModal(
    shouldShow: Boolean,
    closeBlock: (Boolean) -> Unit,
    primaryBlock: () -> Unit
) {
    plutoFeedbackModal {
        titleRes = R.string.details_delete_seed_title
        messageRes = R.string.details_delete_seed_message
        closeButton = { closeBlock(false) }
        primaryButton = {
            buttonTextRes = R.string.common_proceed
            buttonAction = primaryBlock
        }
    }.build(shouldShow, childFragmentManager, TAG_DELETE_SEED_MODAL)
}

internal fun Fragment.showEditAliasModal(
    binding: DetailsModalEditBinding,
    shouldShow: Boolean,
    closeBlock: (Boolean) -> Unit,
    primaryBlock: (String) -> Unit
) {
    plutoFeedbackModal {
        customView = binding.root
        closeButton = { closeBlock(false) }
        primaryButton = {
            buttonTextRes = R.string.common_save
            buttonAction = {
                primaryBlock(binding.inputLayoutAlias.getTextOrHint())
            }
        }
    }.build(shouldShow, childFragmentManager, TAG_EDIT_ALIAS_MODAL)
}

internal fun Fragment.showUnlockSeedErrorDialog(
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
    }.build(shouldShow, childFragmentManager, TAG_DECRYPT_ERROR_DIALOG)
}

internal fun Fragment.showLoadingDialog(shouldShow: Boolean) {
    plutoProgressDialog {
        shouldShowMessage = false
    }.build(shouldShow, childFragmentManager, TAG_LOADING_DIALOG)
}

internal fun Int?.formValidation(text: String): Int? {
    return if (isNull()) {
        R.string.field_required.takeIf { text.isBlank() }
    } else this
}

internal fun View.saveToGalleryAction(resultBlock: (Boolean) -> Unit) {
    saveToGallery(
        fileName = "${FILE_NAME}_${System.currentTimeMillis()}",
        bgColorRes = R.color.plu_white,
        resultBlock = resultBlock
    )
}

internal fun DetailsIncludeSeedBinding.setPalletColors(coloredSeed: List<Pair<String, String>>) {
    val hexColors = coloredSeed.joinToString(DELIMITERS) { it.first }
    val bitmap = PlutoQrcodeCreator.create(hexColors)
    imageQrcode.setImageBitmap(bitmap)

    paletteContainer.run {
        removeAllViews()
        for (colorPair in coloredSeed) {
            val (hexColor, textColor) = colorPair
            val colorView = AppCompatTextView(
                ContextThemeWrapper(context, R.style.PlutoSeedPalette)
            ).apply {
                setBackgroundColor(Color.parseColor(hexColor))
                setTextColor(Color.parseColor(textColor))
                text = hexColor
            }
            addView(colorView)
        }
    }
}

internal fun Fragment.showDeniedStoragePermissionModal(
    shouldShow: Boolean,
    closeBlock: (Boolean) -> Unit,
    primaryBlock: () -> Unit
) {
    plutoFeedbackModal {
        titleRes = R.string.common_storage_permission_denied_title
        messageRes = R.string.common_storage_permission_always_denied_message
        closeButton = { closeBlock(false) }
        primaryButton = {
            buttonTextRes = R.string.common_camera_permission_always_denied_primary_button
            buttonAction = primaryBlock
        }
    }.build(shouldShow, childFragmentManager, TAG_DENIED_STORAGE_PERMISSION_MODAL)
}

internal fun Fragment.permissionLauncher(
    permissionBlock: (status: PermissionStatus) -> Unit
): PermissionLauncher? = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
    permissionLauncher(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        resultBLock = permissionBlock
    )
} else null

internal fun AppCompatTextView.numberedSeed(seed: String) {
    showNumberedSeed(seed, R.color.plu_light_mist_gray)
}
