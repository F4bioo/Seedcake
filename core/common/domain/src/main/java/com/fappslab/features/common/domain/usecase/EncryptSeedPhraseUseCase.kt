package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.model.ParamsFactory
import com.fappslab.features.common.domain.model.TransformationType
import com.fappslab.features.common.domain.repository.ObfuscationRepository

class EncryptSeedPhraseUseCase(
    private val repository: ObfuscationRepository
) {

    suspend operator fun invoke(params: EncryptParams): String {
        return repository.encrypt(params)
    }
}

data class EncryptParams(
    val cipherSpec: TransformationType,
    val readableSeedPhrase: List<String>,
    val passphrase: String
) : ParamsFactory
