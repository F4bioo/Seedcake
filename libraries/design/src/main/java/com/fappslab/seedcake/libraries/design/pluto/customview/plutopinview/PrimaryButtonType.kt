package com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview

import androidx.annotation.StringRes
import com.fappslab.seedcake.libraries.design.R

internal enum class PrimaryButtonType(@StringRes val textRes: Int) {
    Enter(textRes = R.string.common_enter),
    Next(textRes = R.string.common_next),
    Save(textRes = R.string.common_save),
    Unlock(textRes = R.string.common_unlock)
}
