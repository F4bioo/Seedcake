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
        "gesture grow leaf among exclude man spirit bar other column early flavor"

    const val ENCRYPTED =
        "BaqFq2Z9ab4VHC1EDEBH53TZiGIbqIh0TsFsUUhqTr38aF4o50+RnowhXqBO6A8YWovLwfi4uzXTXy0maEnytAqEBw5cqGhun2SanzVIgnH1iFU44oUBJnnvxnbP9StNNLiU3yz1ySw58V9IZxGm4UPiXdo=:ENDeyJjcyI6IkdDTSIsImlzIjoxMiwia2EiOiJBRVMiLCJrYiI6MjU2LCJrZCI6IlBIUzUxMiIsImtpIjoyMDAwMDAsInNzIjoxNn0="

    const val PALETTE =
        "(#3D2206, #FFFFFF), (#B7AA61, #000000), (#DDDFA6, #000000), (#9FEE69, #000000), (#225906, #FFFFFF), (#7CA2A5, #000000), (#0130B8, #FFFFFF), (#6073B7, #FFFFFF)"
    const val COLORED = "#3D2206 #B7AA61 #DDDFA6 #9FEE69 #225906 #7CA2A5 #0130B8 #6073B7"

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
            unreadableSeedPhrase = "a5qjuKiSIR49DL0WeD1caGA6jbIufP/G82hIYpV5b5Evtjj1QNZwsMKGRM6SRgzh…"
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
