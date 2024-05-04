package com.fappslab.features.encrypt.secret.presentation.viewmodel.passphrase

import com.fappslab.features.encrypt.secret.domain.usecase.MIN_PASSPHRASE_LENGTH

internal data class GeneratorViewState(
    val sliderSizeValue: Int = MIN_PASSPHRASE_LENGTH
)
