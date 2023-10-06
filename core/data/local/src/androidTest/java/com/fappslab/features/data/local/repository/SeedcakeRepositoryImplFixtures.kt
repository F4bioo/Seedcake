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
        "abandon banana captain dance eagle fabric galaxy habit ice jacket kangaroo language"

    const val ENCRYPTED =
        "a5qjuKiSIR49DL0WeD1caGA6jbIufP/G82hIYpV5b5Evtjj1QNZwsMKGRM6SRgzh8IYlYVHfDVyBAyuXvCsFsGKuksr8a/7bf9zPwikG7d51Ygtq/wb3L2OHq2mjznO7RTjTf+/aaG7Y/8xLegSwUnaWHRj+Ut9w1Pv/FaiN1A==:ENDeyJjcyI6IkdDTSIsImlzIjoxMiwia2EiOiJBRVMiLCJrYiI6MjU2LCJrZCI6IlBIUzUxMiIsImtpIjoyMDAwMDAsInNzIjoxNn0="

    const val PALETTE =
        "(#B88F4D, #000000), (#9DCA22, #000000), (#3DB611, #FFFFFF), (#63A65C, #000000), (#258A72, #FFFFFF), (#000065, #FFFFFF), (#7B3AE8, #FFFFFF), (#E0C6F8, #000000)"
    const val COLORED = "#B88F4D #9DCA22 #3DB611 #63A65C #258A72 #000065 #7B3AE8 #E0C6F8"

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
            encryptedSeed = "a5qjuKiSIR49DL0WeD1caGA6jbIufP/G82hIYpV5b5Evtjj1QNZwsMKGRM6SRgzh…"
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
