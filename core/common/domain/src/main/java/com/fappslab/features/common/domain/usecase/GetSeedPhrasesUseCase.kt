package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow

class GetSeedPhrasesUseCase(
    private val repository: StorageRepository
) {

    operator fun invoke(): Flow<List<Seed>> =
        repository.getSeedPhrases()
}
