package com.fappslab.seedcake.libraries.design.pluto.fragment.progress

import androidx.lifecycle.LifecycleOwner

@Suppress("unused")
fun LifecycleOwner.plutoProgressDialog(
    block: PlutoProgressDialog.() -> Unit
): PlutoProgressDialog = PlutoProgressDialog().apply(block)
