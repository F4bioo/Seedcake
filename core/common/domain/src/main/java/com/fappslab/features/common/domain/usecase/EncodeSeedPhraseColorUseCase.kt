package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.ObfuscationRepository

class EncodeSeedPhraseColorUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(readableSeedPhrase: String): List<Pair<String, String>> =
        repository.encodeColor(readableSeedPhrase)
}
