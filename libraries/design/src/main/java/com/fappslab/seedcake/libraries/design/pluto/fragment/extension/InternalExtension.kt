package com.fappslab.seedcake.libraries.design.pluto.fragment.extension

import androidx.fragment.app.FragmentManager
import com.fappslab.seedcake.libraries.design.pluto.fragment.dialog.PlutoFeedbackDialog
import com.fappslab.seedcake.libraries.design.pluto.fragment.lock.PlutoLockPage
import com.fappslab.seedcake.libraries.design.pluto.fragment.modal.PlutoFeedbackModal
import com.fappslab.seedcake.libraries.design.pluto.fragment.page.PlutoFeedbackPage
import com.fappslab.seedcake.libraries.design.pluto.fragment.progress.PlutoProgressDialog
import com.fappslab.seedcake.libraries.extension.isNotNull

internal fun FragmentManager.hide(tag: String) {
    if (isShowing(tag)) {
        when (val dialog = findFragmentByTag(tag)) {
            is PlutoFeedbackPage -> dialog.dismissAllowingStateLoss()
            is PlutoLockPage -> dialog.dismissAllowingStateLoss()
            is PlutoFeedbackDialog -> dialog.dismissAllowingStateLoss()
            is PlutoFeedbackModal -> dialog.dismissAllowingStateLoss()
            is PlutoProgressDialog -> dialog.dismissAllowingStateLoss()
        }
    }
}

internal fun FragmentManager.isShowing(tag: String): Boolean {
    executePendingTransactions()
    return findFragmentByTag(tag).isNotNull()
}
