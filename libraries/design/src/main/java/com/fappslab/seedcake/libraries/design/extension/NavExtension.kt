package com.fappslab.seedcake.libraries.design.extension

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.fappslab.seedcake.libraries.design.R

private val animations = NavOptions.Builder()
    .setEnterAnim(R.anim.plu_enter_anim)
    .setExitAnim(R.anim.plu_exit_anim)
    .setPopEnterAnim(R.anim.plu_pop_enter_anim)
    .setPopExitAnim(R.anim.plu_pop_exit_anim)
    .build()

fun NavController.navigateWithAnimations(
    destinationId: Int,
    animation: NavOptions = animations
) {
    navigate(destinationId, null, animation)
}

fun NavController.navigateWithAnimations(
    directions: NavDirections,
    animation: NavOptions = animations
) {
    navigate(directions, animation)
}
