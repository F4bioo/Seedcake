package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.StorageRepository

class GetPinUseCase(
    private val repository: StorageRepository
) {

    operator fun invoke(): String? =
        repository.getPin()
}
