package com.fappslab.features.encrypt.seed.presentation.model

import androidx.annotation.DrawableRes
import com.fappslab.seedcake.features.encrypt.R

internal data class AlgorithmItem(
    val type: AlgorithmType,
    val isExpanded: Boolean = false,
    @DrawableRes val expandableIconRes: Int =
        R.drawable.plu_ic_expand_more
) {

    sealed class OnItem {
        data class Card(val algorithmItem: AlgorithmItem) : OnItem()
        data class Use(val algorithmType: AlgorithmType) : OnItem()
    }
}
