package com.fappslab.seedcake.libraries.design.pluto.fragment.page

import androidx.lifecycle.LifecycleOwner

@Suppress("unused")
fun LifecycleOwner.plutoFeedbackPage(
    block: PlutoFeedbackPage.() -> Unit
): PlutoFeedbackPage = PlutoFeedbackPage().apply(block)
