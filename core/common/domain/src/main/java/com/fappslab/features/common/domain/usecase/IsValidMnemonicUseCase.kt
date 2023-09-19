package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.ObfuscationRepository

class IsValidMnemonicUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(seed: List<String>): Boolean =
        seed.none { it !in repository.getWordList() }
}
