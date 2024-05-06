package com.fappslab.features.data.local.repository

import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.common.domain.usecase.DecodeParams
import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.EncodeParams
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

    override suspend fun encodeColor(params: EncodeParams): List<Pair<String, String>> =
        dataSource.encodeColor(params)

    override suspend fun decodeColor(params: DecodeParams): String =
        dataSource.decodeColor(params)
}
