package com.fappslab.features.common.domain.repository

import com.fappslab.features.common.domain.model.Seed
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
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
