package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.ObfuscationRepository

class EncodeSeedColorUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(readableSeed: String): List<Pair<String, String>> =
        repository.encodeSeedColor(readableSeed)
}
