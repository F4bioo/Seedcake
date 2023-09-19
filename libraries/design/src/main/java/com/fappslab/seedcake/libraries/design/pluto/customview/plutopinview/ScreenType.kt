package com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class ScreenType : Parcelable {
    object Register : ScreenType()
    data class Validate(val savedPin: String) : ScreenType()
}
