package com.fappslab.features.about.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal data class Item(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int,
    val externalLink: String
)
