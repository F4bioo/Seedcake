package com.fappslab.seedcake.libraries.design.pluto.fragment.dialog

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.hide

@Suppress("unused")
fun LifecycleOwner.plutoFeedbackDialog(
    block: PlutoFeedbackDialog.() -> Unit
): PlutoFeedbackDialog = PlutoFeedbackDialog().apply(block)

fun PlutoFeedbackDialog.build(
    shouldShow: Boolean = true,
    manager: FragmentManager,
    tag: String = this::class.java.simpleName
) {
    manager.hide(tag)
    if (shouldShow) {
        show(manager, tag)
    }
}
