package com.fappslab.features.data.local.repository

import android.content.Context
import android.content.SharedPreferences
import app.cash.turbine.test
import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.data.local.db.SeedcakeDatabase
import com.fappslab.features.data.local.db.client.SecurePreferences
import com.fappslab.features.data.local.model.SourceProvider
import com.fappslab.features.data.local.source.SeedcakeDataSourceImpl
import com.fappslab.libraries.security.bip39colors.BIP39ColorsImpl
import com.fappslab.libraries.security.bip39words.Bip39WordsImpl
import com.fappslab.libraries.security.cryptography.CryptoManagerImpl
import com.fappslab.seedcake.core.data.local.BuildConfig
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal object SeedcakeRepositoryImplFixtures {

    const val SEED =
        "advance above add acoustic almost ability action adjust across alert absurd abandon"

    const val ENCRYPTED =
        "ugJSPh2YxlUMSeTJn8n1ubXCEhSkf88ya1RxJAn4GmYVEss05x/jwCoanmHwF+43AesOsgiYrmVw9z7hOwptHwMEr6Al+7w9rQ7rFaGDSkUBrmGwxYDBSxyUYxSfIJzrAOdI1JMVuok7g+161399aA=="

    const val PALETTE =
        "(#D6FF11, #000000), (#63F1F2, #000000), (#1F47EA, #FFFFFF), (#000C80, #FFFFFF), (#3D0FA4, #FFFFFF), (#B72E88, #FFFFFF), (#9D0363, #FFFFFF), (#7A1A34, #FFFFFF)"
    const val COLORED = "#D6FF11 #63F1F2 #1F47EA #000C80 #3D0FA4 #B72E88 #9D0363 #7A1A34"

    const val PASSPHRASE = "Seedc@k3"

    suspend fun StorageRepository.setupDatabaseWithItem() {
        // Prepare the database by inserting a value
        setSeedPhrase(seedStub())

        // Assert that the value was inserted
        getSeedPhrases().test {
            assertEquals(listOf(seedStub()), awaitItem())
        }
    }

    fun StorageRepository.setupSecurePreferencesWithItem() {
        // Prepare the secure preferences by inserting a value
        val pin = "4819"
        setPin(pin)

        // Assert that the value was inserted
        assertEquals(pin, getPin())
    }

    fun SharedPreferences.simplePreferencesWithItem(prefsKey: String) {
        // Prepare the simple preferences by inserting a value
        edit().putBoolean(prefsKey, true).apply()

        // Assert that the value was inserted
        assertTrue(getBoolean(prefsKey, false))
    }

    fun seedStub() =
        Seed(
            id = 1,
            alias = "Trezor wallet",
            date = "10-Aug-2023, 02:15 PM",
            encryptedSeed = "UMDJ3sMCSF9q42IGeFmkSrUhpckbUiVPZR06eBGEApI6uvYKFdADW6i…"
        )

    fun String.toColorPairs(): List<Pair<String, String>> {
        val regex = "\\((#[0-9A-Fa-f]+),\\s?(#[0-9A-Fa-f]+)\\)".toRegex()
        return regex.findAll(this).map { matchResult ->
            val (hexColor, textColor) = matchResult.destructured
            hexColor to textColor
        }.toList()
    }

    fun createDataSource(params: InjectParams): SeedcakeDataSourceImpl {
        val bip39Words = Bip39WordsImpl(params.context)
        return SeedcakeDataSourceImpl(
            provider = SourceProvider(
                bip39Words = bip39Words,
                cryptoManager = CryptoManagerImpl(bip39Words.wordList()),
                bIP39Colors = BIP39ColorsImpl(bip39Words.wordList()),
                dao = params.database.seedcakeDao(),
                securePreferences = SecurePreferences(
                    params.context, BuildConfig.SECURE_PREFS_FILE_NAME
                ).create(),
                simplePreferences = params.simplePreferences
            )
        )
    }

    data class InjectParams(
        val context: Context,
        val database: SeedcakeDatabase = mockk(relaxed = true),
        val simplePreferences: SharedPreferences = mockk(relaxed = true)
    )
}
