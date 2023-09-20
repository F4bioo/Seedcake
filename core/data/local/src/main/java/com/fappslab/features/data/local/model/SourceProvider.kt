package com.fappslab.features.data.local.model

import android.content.SharedPreferences
import com.fappslab.features.data.local.db.SeedcakeDao
import com.fappslab.libraries.security.bip39colors.BIP39Colors
import com.fappslab.libraries.security.bip39words.Bip39Words
import com.fappslab.libraries.security.cryptography.CryptoManager

internal data class SourceProvider(
    val cryptoManager: CryptoManager,
    val bip39Words: Bip39Words,
    val bIP39Colors: BIP39Colors,
    val dao: SeedcakeDao,
    val securePreferences: SharedPreferences,
    val simplePreferences: SharedPreferences
)
