package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.StorageRepository

class DeletePinUseCase(
    private val repository: StorageRepository
) {

    operator fun invoke() {
        repository.deletePin()
    }
}
