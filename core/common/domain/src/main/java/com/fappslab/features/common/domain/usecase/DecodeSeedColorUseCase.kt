package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.ObfuscationRepository

class DecodeSeedColorUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(coloredSeed: String): String =
        repository.decodeSeedColor(coloredSeed)
}
