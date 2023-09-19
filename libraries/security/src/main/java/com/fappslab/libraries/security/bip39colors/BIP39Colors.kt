package com.fappslab.libraries.security.bip39colors

interface BIP39Colors {
    suspend fun encodeSeedColor(readableSeed: String): List<Pair<String, String>>
    suspend fun decodeSeedColor(coloredSeed: String): String
}
