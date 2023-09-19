package com.fappslab.seedcake.libraries.design.pluto.fragment.modal

import androidx.annotation.StringRes

class PlutoFeedbackModalButtonConfig {
    @StringRes
    var buttonTextRes: Int? = null
    var buttonText: String? = null
    var buttonAction: () -> Unit = {}
}
