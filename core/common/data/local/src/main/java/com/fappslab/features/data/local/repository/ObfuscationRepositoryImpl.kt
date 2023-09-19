package com.fappslab.features.data.local.repository

import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.data.local.source.SeedcakeDataSource

internal class ObfuscationRepositoryImpl(
    private val dataSource: SeedcakeDataSource
) : ObfuscationRepository {

    override suspend fun getWordList(): List<String> =
        dataSource.getWordList()

    override suspend fun encrypt(seed: List<String>, passphrase: String): String =
        dataSource.encrypt(seed, passphrase)

    override suspend fun decrypt(encryptedSeed: String, passphrase: String): String =
        dataSource.decrypt(encryptedSeed, passphrase)

    override suspend fun encodeSeedColor(readableSeed: String): List<Pair<String, String>> =
        dataSource.encodeSeedColor(readableSeed)

    override suspend fun decodeSeedColor(coloredSeed: String): String =
        dataSource.decodeSeedColor(coloredSeed)
}
