package com.fappslab.features.common.domain.repository

import com.fappslab.features.common.domain.usecase.DecodeParams
import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.EncodeParams
import com.fappslab.features.common.domain.usecase.EncryptParams

interface ObfuscationRepository {
    // Word list provider
    suspend fun getWordList(): List<String>

    // Obfuscation
    suspend fun encrypt(params: EncryptParams): String
    suspend fun decrypt(params: DecryptParams): String
    suspend fun encodeColor(params: EncodeParams): List<Pair<String, String>>
    suspend fun decodeColor(params: DecodeParams): String
}
