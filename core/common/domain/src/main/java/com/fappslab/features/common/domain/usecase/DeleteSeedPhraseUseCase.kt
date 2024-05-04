package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.StorageRepository

class DeleteSeedPhraseUseCase(
    private val repository: StorageRepository
) {

    suspend operator fun invoke(id: Int) {
        repository.deleteSeedPhrase(id)
    }
}
