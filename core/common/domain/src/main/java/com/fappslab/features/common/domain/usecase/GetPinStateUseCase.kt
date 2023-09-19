package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.StorageRepository

class GetPinStateUseCase(
    private val repository: StorageRepository
) {

    operator fun invoke(): Boolean =
        repository.getPinCheckBoxState()
}
