package com.fappslab.features.decrypt.presentation.extension

import android.Manifest
import android.widget.EditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.fappslab.seedcake.features.decrypt.R
import com.fappslab.seedcake.libraries.arch.simplepermission.extension.permissionLauncher
import com.fappslab.seedcake.libraries.arch.simplepermission.launcher.PermissionLauncher
import com.fappslab.seedcake.libraries.arch.simplepermission.model.PermissionStatus
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.GravityType
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.plutoFeedbackDialog
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.modal.plutoFeedbackModal
import com.fappslab.seedcake.libraries.design.pluto.fragment.progress.plutoProgressDialog
import com.fappslab.seedcake.libraries.extension.showNumberedSeed
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputLayout

private const val TAG_DENIED_CAMERA_PERMISSION_MODAL = "showDeniedCameraPermissionModal"
private const val TAG_DECRYPT_ERROR_DIALOG = "showUnlockSeedErrorDialog"
private const val TAG_LOADING_DIALOG = "showLoadingDialog"

internal fun Fragment.showDeniedCameraPermissionModal(
    shouldShow: Boolean,
    closeBlock: (Boolean) -> Unit,
    primaryBlock: () -> Unit
) {
    plutoFeedbackModal {
        titleRes = R.string.common_camera_permission_denied_title
        messageRes = R.string.common_camera_permission_always_denied_message
        closeButton = { closeBlock(false) }
        primaryButton = {
            buttonTextRes = R.string.common_camera_permission_always_denied_primary_button
            buttonAction = primaryBlock
        }
    }.build(shouldShow, childFragmentManager, TAG_DENIED_CAMERA_PERMISSION_MODAL)
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

internal fun EditText.onAfterTextChanged(changeBLock: (String) -> Unit) {
    doAfterTextChanged { changeBLock(it.toString()) }
}

internal fun ViewPager2.setTabName(tabLayout: TabLayout) {
    val texts = listOf(
        context.getString(R.string.decrypt_unlock_encrypted_seed),
        context.getString(R.string.decrypt_unlock_colored_seed)
    )
    TabLayoutMediator(tabLayout, this) { tab, position ->
        tab.text = texts[position]
    }.attach()
}

internal fun Fragment.permissionLauncher(
    permissionBlock: (status: PermissionStatus) -> Unit
): PermissionLauncher = permissionLauncher(
    Manifest.permission.CAMERA,
    resultBLock = permissionBlock
)

internal fun AppCompatTextView.numberedSeed(readableSeed: String) {
    showNumberedSeed(readableSeed, R.color.plu_light_mist_gray)
}

fun TextInputLayout.inputError(errorPair: Pair<Int, String?>?) {
    error = errorPair?.let {
        val (messageRes, placeholder) = it
        context.getString(messageRes, placeholder)
    }
}
