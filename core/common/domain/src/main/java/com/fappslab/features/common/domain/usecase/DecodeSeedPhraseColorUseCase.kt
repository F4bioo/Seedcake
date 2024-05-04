package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.ObfuscationRepository

class DecodeSeedPhraseColorUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(colorfulSeedPhrase: String): String =
        repository.decodeColor(colorfulSeedPhrase)
}
