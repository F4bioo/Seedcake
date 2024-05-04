package com.fappslab.features.encrypt.secret.presentation.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.fappslab.features.encrypt.secret.domain.usecase.MIN_PASSPHRASE_LENGTH
import com.fappslab.features.encrypt.secret.presentation.GeneratorBottomSheet
import com.fappslab.features.encrypt.secret.presentation.model.FormParams
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.features.encrypt.databinding.EncryptFragmentSecretBinding
import com.fappslab.seedcake.libraries.extension.getColorRes
import com.fappslab.seedcake.libraries.extension.getTextOrHint
import com.fappslab.seedcake.libraries.extension.isNull
import com.fappslab.seedcake.libraries.extension.isValidPassphrase

private const val ANIMATION_ROTATION_START = 0f
private const val ANIMATION_ROTATION_END = 360f
private const val ANIMATION_DURATION = 500L

private const val ORANGE_MIN_LENGTH = 9
private const val ORANGE_MAX_LENGTH = 10
private const val YELLOW_ORANGE_MIN_LENGTH = 11
private const val YELLOW_ORANGE_MAX_LENGTH = 12

internal fun EncryptFragmentSecretBinding.getFormParams(): FormParams {
    return FormParams(
        alias = inputLayoutAlias.getTextOrHint(),
        passphrase1 = inputPassphrase1.text.toString(),
        passphrase2 = inputPassphrase2.text.toString()
    )
}

internal fun Int?.formValidation(text: String): Int? {
    return if (isNull()) {
        R.string.field_required.takeIf { text.isBlank() }
    } else this
}

internal fun String.toErrorRes(): Int? {
    return takeUnless { it.isEmpty() }
        ?.takeIf { it.isValidPassphrase().not() }
        ?.let { R.string.encrypt_passphrase_strength_requirements }
}

internal fun View.rotateButton() {
    val rotateAnimation = ObjectAnimator.ofFloat(
        this, "rotation",
        ANIMATION_ROTATION_START, ANIMATION_ROTATION_END
    )
    rotateAnimation.duration = ANIMATION_DURATION
    rotateAnimation.interpolator = AccelerateDecelerateInterpolator()
    rotateAnimation.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            rotation = ANIMATION_ROTATION_START
        }
    })
    rotateAnimation.start()
}

internal fun GeneratorBottomSheet.getStrengthColor(length: Int): Int {
    val colorRes = when (length) {
        MIN_PASSPHRASE_LENGTH -> R.color.color_red
        in ORANGE_MIN_LENGTH..ORANGE_MAX_LENGTH -> R.color.color_orange
        in YELLOW_ORANGE_MIN_LENGTH..YELLOW_ORANGE_MAX_LENGTH -> R.color.color_yellow_orange
        else -> R.color.color_dark_green
    }
    return requireContext().getColorRes(colorRes)
}
