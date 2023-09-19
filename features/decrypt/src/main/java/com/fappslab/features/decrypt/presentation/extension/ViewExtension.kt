package com.fappslab.features.decrypt.presentation.extension

import android.Manifest
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.fappslab.seedcake.features.decrypt.R
import com.fappslab.seedcake.libraries.arch.simplepermission.extension.permissionLauncher
import com.fappslab.seedcake.libraries.arch.simplepermission.launcher.PermissionLauncher
import com.fappslab.seedcake.libraries.arch.simplepermission.model.PermissionStatus
import com.fappslab.seedcake.libraries.design.pluto.fragment.modal.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.modal.plutoFeedbackModal
import com.fappslab.seedcake.libraries.design.pluto.fragment.progress.build
import com.fappslab.seedcake.libraries.design.pluto.fragment.progress.plutoProgressDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val TAG_DENIED_CAMERA_PERMISSION_MODAL = "showDeniedCameraPermissionModal"
private const val TAG_DECRYPT_ERROR_MODAL = "showDecryptErrorModal"
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

internal fun Fragment.showUnlockSeedErrorModal(
    @StringRes errorMessageRes: Int?,
    shouldShow: Boolean,
    closeBlock: (Boolean) -> Unit
) {
    plutoFeedbackModal {
        titleRes = R.string.common_error_title
        messageRes = errorMessageRes
        closeButton = { closeBlock(false) }
        primaryButton = {
            buttonTextRes = R.string.common_try_again
            buttonAction = { closeBlock(false) }
        }
    }.build(shouldShow, childFragmentManager, TAG_DECRYPT_ERROR_MODAL)
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
        context.getString(R.string.unlock_encrypted_seed),
        context.getString(R.string.unlock_colored_seed)
    )
    TabLayoutMediator(tabLayout, this) { tab, position ->
        tab.text = texts[position]
    }.attach()
}

fun Fragment.permissionLauncher(
    permissionBlock: (status: PermissionStatus) -> Unit
): PermissionLauncher = permissionLauncher(
    Manifest.permission.CAMERA,
    resultBLock = permissionBlock
)
