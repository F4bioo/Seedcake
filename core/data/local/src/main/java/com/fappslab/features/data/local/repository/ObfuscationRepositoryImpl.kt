package com.fappslab.features.data.local.repository

import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.EncryptParams
import com.fappslab.features.data.local.source.SeedcakeDataSource

internal class ObfuscationRepositoryImpl(
    private val dataSource: SeedcakeDataSource
) : ObfuscationRepository {

    override suspend fun getWordList(): List<String> =
        dataSource.getWordList()

    override suspend fun encrypt(params: EncryptParams): String =
        dataSource.encrypt(params)

    override suspend fun decrypt(params: DecryptParams): String =
        dataSource.decrypt(params)

    override suspend fun encodeColor(readableSeedPhrase: String): List<Pair<String, String>> =
        dataSource.encodeColor(readableSeedPhrase)

    override suspend fun decodeColor(colorfulSeedPhrase: String): String =
        dataSource.decodeColor(colorfulSeedPhrase)
}
