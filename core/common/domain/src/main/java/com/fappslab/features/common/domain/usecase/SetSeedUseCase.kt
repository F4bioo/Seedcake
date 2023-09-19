package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.domain.repository.StorageRepository

class SetSeedUseCase(
    private val repository: StorageRepository
) {

    suspend operator fun invoke(seed: Seed) {
        repository.setSeedPhrase(seed)
    }
}
