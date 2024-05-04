package com.fappslab.features.common.domain.repository

import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.EncryptParams

interface ObfuscationRepository {
    // Word list provider
    suspend fun getWordList(): List<String>

    // Obfuscation
    suspend fun encrypt(params: EncryptParams): String
    suspend fun decrypt(params: DecryptParams): String
    suspend fun encodeColor(readableSeedPhrase: String): List<Pair<String, String>>
    suspend fun decodeColor(colorfulSeedPhrase: String): String
}
