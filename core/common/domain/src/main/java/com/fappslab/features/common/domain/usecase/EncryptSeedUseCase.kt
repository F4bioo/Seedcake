package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.ObfuscationRepository

class EncryptSeedUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(seed: List<String>, passphrase: String): String =
        repository.encrypt(seed, passphrase)
}
