package com.fappslab.features.decrypt.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal enum class PageType : Parcelable {
    EncryptedSeed,
    ColoredSeed
}
