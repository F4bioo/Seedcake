package com.fappslab.features.about.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fappslab.seedcake.features.about.R

internal data class Header(
    @DrawableRes val bgRes: Int = R.drawable.ic_launcher_background,
    @DrawableRes val imageRes: Int = R.drawable.ic_launcher_foreground,
    @StringRes val titleRes: Int = R.string.subtitle_about,
    @StringRes val buttonTextRes: Int = R.string.review_about
)
