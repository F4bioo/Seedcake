package com.fappslab.features.data.local.repository

import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.data.local.source.SeedcakeDataSource
import kotlinx.coroutines.flow.Flow

internal class StorageRepositoryImpl(
    private val dataSource: SeedcakeDataSource
) : StorageRepository {

    override suspend fun setSeedPhrase(seed: Seed) =
        dataSource.setSeedPhrase(seed)

    override suspend fun deleteSeedPhrase(id: Int) =
        dataSource.deleteSeedPhrase(id)

    override fun getSeedPhrases(): Flow<List<Seed>> =
        dataSource.getSeedPhrases()

    override fun setPin(pin: String) {
        dataSource.setPin(pin)
    }

    override fun getPin(): String? =
        dataSource.getPin()

    override fun deletePin() {
        dataSource.deletePin()
    }

    override fun getPinCheckBoxState(): Boolean =
        dataSource.getPinCheckBoxState()

    override fun getFingerprintCheckBoxState(): Boolean =
        dataSource.getFingerprintCheckBoxState()

    override fun getScreenShieldCheckBoxState(): Boolean =
        dataSource.getScreenShieldCheckBoxState()
}
