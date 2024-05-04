package com.fappslab.libraries.security.cryptography

import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.EncryptParams

interface CryptoManager {
    suspend fun encrypt(params: EncryptParams): String
    suspend fun decrypt(params: DecryptParams): String
}
