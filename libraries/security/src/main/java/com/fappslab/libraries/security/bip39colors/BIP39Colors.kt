package com.fappslab.libraries.security.bip39colors

interface BIP39Colors {
    suspend fun encodeSeedColor(readableSeedPhrase: String): List<Pair<String, String>>
    suspend fun decodeSeedColor(colorfulSeedPhrase: String): String
}
