package com.fappslab.features.common.domain.repository

interface ObfuscationRepository {
    // Word list provider
    suspend fun getWordList(): List<String>

    // Obfuscation
    suspend fun encrypt(seed: List<String>, passphrase: String): String
    suspend fun decrypt(encryptedSeed: String, passphrase: String): String
    suspend fun encodeSeedColor(readableSeed: String): List<Pair<String, String>>
    suspend fun decodeSeedColor(coloredSeed: String): String
}
