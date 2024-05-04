package com.fappslab.seedcake.libraries.design.pluto.fragment.modal

import androidx.lifecycle.LifecycleOwner

@Suppress("unused")
fun LifecycleOwner.plutoFeedbackModal(
    block: PlutoFeedbackModal.() -> Unit
): PlutoFeedbackModal = PlutoFeedbackModal().apply(block)
