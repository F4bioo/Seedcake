package com.fappslab.features.encrypt.seed.presentation.component

import com.fappslab.seedcake.features.encrypt.R

internal enum class InputType(val count: Int, val radioIdRes: Int) {
    TWELVE(count = 12, radioIdRes = R.id.radio_twelve),
    TWENTY_FOUR(count = 24, radioIdRes = R.id.radio_twenty_four)
}
