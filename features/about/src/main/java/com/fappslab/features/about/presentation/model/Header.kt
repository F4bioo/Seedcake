package com.fappslab.features.about.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fappslab.seedcake.features.about.R

internal data class Header(
    @DrawableRes val bgRes: Int = R.drawable.ic_launcher_background,
    @DrawableRes val imageRes: Int = R.drawable.ic_launcher_foreground,
    @StringRes val titleRes: Int = R.string.about_subtitle_about,
    @StringRes val buttonTextRes: Int = R.string.about_review_about
)
