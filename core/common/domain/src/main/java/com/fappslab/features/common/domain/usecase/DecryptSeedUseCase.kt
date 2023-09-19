package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.ObfuscationRepository

class DecryptSeedUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(encryptedSeed: String, passphrase: String): String =
        repository.decrypt(encryptedSeed, passphrase)
}
