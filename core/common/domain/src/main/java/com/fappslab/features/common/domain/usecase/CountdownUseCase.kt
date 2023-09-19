package com.fappslab.features.common.domain.usecase

import kotlinx.coroutines.delay

class CountdownUseCase {

    suspend operator fun invoke(delay: Long, timeoutBlock: () -> Unit) {
        delay(delay)
        timeoutBlock()
    }
}
