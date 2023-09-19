package com.fappslab.features.data.local.source

import com.fappslab.features.common.domain.model.Seed
import kotlinx.coroutines.flow.Flow

internal interface SeedcakeDataSource {
    // Word list provider
    suspend fun getWordList(): List<String>

    // Obfuscation
    suspend fun encrypt(seed: List<String>, passphrase: String): String
    suspend fun decrypt(encryptedSeed: String, passphrase: String): String
    suspend fun encodeSeedColor(readableSeed: String): List<Pair<String, String>>
    suspend fun decodeSeedColor(coloredSeed: String): String

    // Room database
    suspend fun setSeedPhrase(seed: Seed)
    suspend fun deleteSeedPhrase(id: Int)
    fun getSeedPhrases(): Flow<List<Seed>>

    // Security preferences
    fun setPin(pin: String)
    fun getPin(): String?
    fun deletePin()

    // Shared preferences
    fun getPinCheckBoxState(): Boolean
    fun getFingerprintCheckBoxState(): Boolean
    fun getScreenShieldCheckBoxState(): Boolean
}
