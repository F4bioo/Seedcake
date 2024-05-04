package com.fappslab.seedcake.libraries.design.pluto.fragment.dialog

import androidx.lifecycle.LifecycleOwner

@Suppress("unused")
fun LifecycleOwner.plutoFeedbackDialog(
    block: PlutoFeedbackDialog.() -> Unit
): PlutoFeedbackDialog = PlutoFeedbackDialog().apply(block)
