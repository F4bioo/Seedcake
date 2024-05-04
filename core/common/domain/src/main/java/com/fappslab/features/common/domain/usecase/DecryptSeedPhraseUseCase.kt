package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.ObfuscationRepository

class DecryptSeedPhraseUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(params: DecryptParams): String =
        repository.decrypt(params)
}

data class DecryptParams(
    val unreadableSeedPhrase: String,
    val passphrase: String
)
