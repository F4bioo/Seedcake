package com.fappslab.seedcake.libraries.design.pluto.fragment.progress

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.hide

@Suppress("unused")
fun LifecycleOwner.plutoProgressDialog(
    block: PlutoProgressDialog.() -> Unit
): PlutoProgressDialog = PlutoProgressDialog().apply(block)

fun PlutoProgressDialog.build(
    shouldShow: Boolean = true,
    manager: FragmentManager,
    tag: String = this::class.java.simpleName
) {
    manager.hide(tag)
    if (shouldShow) {
        show(manager, tag)
    }
}
