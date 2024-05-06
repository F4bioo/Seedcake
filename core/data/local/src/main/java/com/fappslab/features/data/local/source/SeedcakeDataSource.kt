package com.fappslab.features.data.local.source

import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.domain.usecase.DecodeParams
import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.EncodeParams
import com.fappslab.features.common.domain.usecase.EncryptParams
import kotlinx.coroutines.flow.Flow

internal interface SeedcakeDataSource {
    // Word list provider
    suspend fun getWordList(): List<String>

    // Obfuscation
    suspend fun encrypt(params: EncryptParams): String
    suspend fun decrypt(params: DecryptParams): String
    suspend fun encodeColor(params: EncodeParams): List<Pair<String, String>>
    suspend fun decodeColor(params: DecodeParams): String

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
    fun getShufflePinCheckBoxState(): Boolean
    fun getFingerprintCheckBoxState(): Boolean
    fun getScreenShieldCheckBoxState(): Boolean
}
