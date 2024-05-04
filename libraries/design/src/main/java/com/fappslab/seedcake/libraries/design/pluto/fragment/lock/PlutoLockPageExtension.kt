package com.fappslab.seedcake.libraries.design.pluto.fragment.lock

import androidx.lifecycle.LifecycleOwner
import com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview.ScreenType

@Suppress("unused")
fun LifecycleOwner.plutoLockPage(
    screenType: ScreenType,
    block: PlutoLockPage.() -> Unit
): PlutoLockPage = PlutoLockPage
    .createFragment(screenType)
    .apply(block)
