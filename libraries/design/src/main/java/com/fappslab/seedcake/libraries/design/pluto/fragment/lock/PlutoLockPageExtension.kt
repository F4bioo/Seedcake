package com.fappslab.seedcake.libraries.design.pluto.fragment.lock

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.ScreenType
import com.fappslab.seedcake.libraries.design.pluto.fragment.extension.hide

@Suppress("unused")
fun LifecycleOwner.plutoLockPage(
    screenType: ScreenType,
    block: PlutoLockPage.() -> Unit
): PlutoLockPage = PlutoLockPage
    .createFragment(screenType)
    .apply(block)

fun PlutoLockPage.build(
    shouldShow: Boolean = true,
    manager: FragmentManager,
    tag: String = this::class.java.simpleName
) {
    manager.hide(tag)
    if (shouldShow) {
        show(manager, tag)
    }
}
