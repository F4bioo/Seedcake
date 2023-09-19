package com.fappslab.seedcake.libraries.design.pluto.customview.plutopinview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PinResult : Parcelable {
    object Validate : PinResult()
    data class Register(val pin: String) : PinResult()
}
