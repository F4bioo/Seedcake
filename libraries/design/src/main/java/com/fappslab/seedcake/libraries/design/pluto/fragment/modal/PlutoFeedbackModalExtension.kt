package com.fappslab.seedcake.libraries.design.pluto.fragment.modal

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.hide

@Suppress("unused")
fun LifecycleOwner.plutoFeedbackModal(
    block: PlutoFeedbackModal.() -> Unit
): PlutoFeedbackModal = PlutoFeedbackModal().apply(block)

fun PlutoFeedbackModal.build(
    shouldShow: Boolean = true,
    manager: FragmentManager,
    tag: String = this::class.java.simpleName
) {
    manager.hide(tag)
    if (shouldShow) {
        show(manager, tag)
    }
}
