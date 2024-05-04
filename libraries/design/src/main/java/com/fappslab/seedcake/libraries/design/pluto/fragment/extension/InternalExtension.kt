package com.fappslab.seedcake.libraries.design.pluto.fragment.extension

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.fappslab.seedcake.libraries.extension.isNotNull

fun DialogFragment.build(
    shouldShow: Boolean = true,
    manager: FragmentManager,
    tag: String = this::class.java.simpleName
) {
    manager.hide(tag)
    if (shouldShow) {
        show(manager, tag)
    }
}

private fun FragmentManager.hide(tag: String) {
    if (isShowing(tag)) {
        (findFragmentByTag(tag) as? DialogFragment)?.dismissAllowingStateLoss()
    }
}

private fun FragmentManager.isShowing(tag: String): Boolean {
    executePendingTransactions()
    return findFragmentByTag(tag).isNotNull()
}
