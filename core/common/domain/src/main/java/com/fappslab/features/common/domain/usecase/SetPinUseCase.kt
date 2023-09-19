package com.fappslab.features.common.domain.usecase

import com.fappslab.features.common.domain.repository.StorageRepository

class SetPinUseCase(
    private val repository: StorageRepository
) {

    operator fun invoke(pin: String) {
        repository.setPin(pin)
    }
}
