package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.model.ParamsFactory
import com.fappslab.features.common.domain.repository.ObfuscationRepository

class DecodeSeedPhraseColorUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(params: DecodeParams): String =
        repository.decodeColor(params)
}

data class DecodeParams(
    val colorfulSeedPhrase: List<String>
) : ParamsFactory
