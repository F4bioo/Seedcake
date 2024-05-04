package com.fappslab.features.data.local.source

import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.EncryptParams
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

    override suspend fun encrypt(params: EncryptParams): String =
        withContext(dispatcher) {
            provider.cryptoManager.encrypt(params)
        }

    override suspend fun decrypt(params: DecryptParams): String =
        withContext(dispatcher) {
            provider.cryptoManager.decrypt(params)
        }

    override suspend fun encodeColor(readableSeedPhrase: String): List<Pair<String, String>> =
        withContext(dispatcher) {
            provider.bIP39Colors.encodeSeedColor(readableSeedPhrase)
        }

    override suspend fun decodeColor(colorfulSeedPhrase: String): String =
        withContext(dispatcher) {
            provider.bIP39Colors.decodeSeedColor(colorfulSeedPhrase)
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

    override fun getShufflePinCheckBoxState(): Boolean =
        provider.simplePreferences
            .getBoolean(BuildConfig.SIMPLE_PREFS_SHUFFLE_PIN, true)

    override fun getFingerprintCheckBoxState(): Boolean =
        provider.simplePreferences
            .getBoolean(BuildConfig.SIMPLE_PREFS_FINGERPRINT_KEY, false)

    override fun getScreenShieldCheckBoxState(): Boolean =
        provider.simplePreferences
            .getBoolean(BuildConfig.SIMPLE_PREFS_SCREEN_SHIELD, true)
}
