package com.fappslab.seedcake.libraries.design.pluto.fragment.page

import androidx.annotation.StringRes

class PlutoFeedbackPageButtonConfig {
    @StringRes
    var buttonTextRes: Int? = null
    var buttonText: String? = null
    var buttonAction: () -> Unit = {}
}
