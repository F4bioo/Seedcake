package com.fappslab.seedcake.libraries.design.pluto.fragment.page

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.hide

@Suppress("unused")
fun LifecycleOwner.plutoFeedbackPage(
    block: PlutoFeedbackPage.() -> Unit
): PlutoFeedbackPage = PlutoFeedbackPage().apply(block)

fun PlutoFeedbackPage.build(
    shouldShow: Boolean = true,
    manager: FragmentManager,
    tag: String = this::class.java.simpleName
) {
    manager.hide(tag)
    if (shouldShow) {
        show(manager, tag)
    }
}
