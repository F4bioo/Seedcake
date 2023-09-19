package com.fappslab.features.home.presentation.viewmodel

import com.fappslab.features.common.domain.model.Seed

internal const val PLACEHOLDER_CHILD = 0
internal const val POPULATED_CHILD = 1

internal data class HomeViewState(
    val childPosition: Int = PLACEHOLDER_CHILD,
    val shouldShowLoading: Boolean = true,
    val seeds: List<Seed>? = null
) {

    fun submitList(seeds: List<Seed>) = copy(
        seeds = seeds,
        childPosition = seeds.getChildPosition()
    )

    private fun List<Seed>.getChildPosition(): Int =
        if (isNullOrEmpty()) {
            PLACEHOLDER_CHILD
        } else POPULATED_CHILD
}
