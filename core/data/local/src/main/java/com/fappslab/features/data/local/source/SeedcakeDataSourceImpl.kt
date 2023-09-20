package com.fappslab.features.data.local.source

import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.data.local.model.SourceProvider
import com.fappslab.features.data.local.model.extension.toSeedEntity
import com.fappslab.features.data.local.model.extension.toSeeds
import com.fappslab.seedcake.core.data.local.BuildConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class SeedcakeDataSourceImpl(
    private val provider: SourceProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SeedcakeDataSource {

    override suspend fun getWordList(): List<String> =
        withContext(dispatcher) {
            runCatching { provider.bip39Words.wordList() }
                .getOrElse { emptyList() }
        }

    override suspend fun encrypt(seed: List<String>, passphrase: String): String =
        withContext(dispatcher) {
            provider.cryptoManager.encrypt(seed, passphrase)
        }

    override suspend fun decrypt(encryptedSeed: String, passphrase: String): String =
        withContext(dispatcher) {
            provider.cryptoManager.decrypt(encryptedSeed, passphrase)
        }

    override suspend fun encodeSeedColor(readableSeed: String): List<Pair<String, String>> =
        withContext(dispatcher) {
            provider.bIP39Colors.encodeSeedColor(readableSeed)
        }

    override suspend fun decodeSeedColor(coloredSeed: String): String =
        withContext(dispatcher) {
            provider.bIP39Colors.decodeSeedColor(coloredSeed)
        }

    override suspend fun setSeedPhrase(seed: Seed) =
        withContext(dispatcher) {
            provider.dao.setSeedPhrase(seed.toSeedEntity())
        }

    override suspend fun deleteSeedPhrase(id: Int) =
        withContext(dispatcher) {
            provider.dao.deleteSeedPhrase(id)
        }

    override fun getSeedPhrases(): Flow<List<Seed>> =
        provider.dao.getSeedPhrases()
            .flowOn(dispatcher)
            .map { it.toSeeds() }

    override fun setPin(pin: String) {
        provider.securePreferences.edit()
            .putString(BuildConfig.SECURE_PREFS_PIN_KEY, pin)
            .apply()
    }

    override fun getPin(): String? =
        provider.securePreferences
            .getString(BuildConfig.SECURE_PREFS_PIN_KEY, null)

    override fun deletePin() {
        provider.securePreferences.edit()
            .remove(BuildConfig.SECURE_PREFS_PIN_KEY)
            .apply()
    }

    override fun getPinCheckBoxState(): Boolean =
        provider.simplePreferences
            .getBoolean(BuildConfig.SIMPLE_PREFS_PIN_KEY, false)

    override fun getFingerprintCheckBoxState(): Boolean =
        provider.simplePreferences
            .getBoolean(BuildConfig.SIMPLE_PREFS_FINGERPRINT_KEY, false)

    override fun getScreenShieldCheckBoxState(): Boolean =
        provider.simplePreferences
            .getBoolean(BuildConfig.SIMPLE_PREFS_SCREEN_SHIELD, true)
}
