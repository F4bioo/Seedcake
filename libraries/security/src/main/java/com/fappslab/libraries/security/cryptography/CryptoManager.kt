package com.fappslab.libraries.security.cryptography

interface CryptoManager {
    suspend fun encrypt(seed: List<String>, passphrase: String): String
    suspend fun decrypt(encryptedSeed: String, passphrase: String): String
}
