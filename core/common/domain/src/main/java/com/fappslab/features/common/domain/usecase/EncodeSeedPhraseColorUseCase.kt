package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.model.ParamsFactory
import com.fappslab.features.common.domain.repository.ObfuscationRepository

class EncodeSeedPhraseColorUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(params: EncodeParams): List<Pair<String, String>> =
        repository.encodeColor(params)
}

data class EncodeParams(
    val readableSeedPhrase: List<String>
) : ParamsFactory
