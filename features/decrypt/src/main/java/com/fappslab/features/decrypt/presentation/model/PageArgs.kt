package com.fappslab.features.decrypt.presentation.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class PageArgs(
    val pageType: PageType,
    @StringRes val hintTextRes: Int,
    @StringRes val placeholderTextRes: Int,
    @StringRes val eyeDescriptionTextRes: Int,
    @StringRes val primaryButtonTextRes: Int
) : Parcelable
