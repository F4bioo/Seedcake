package com.fappslab.features.lock.domain.provider

import com.fappslab.features.common.domain.usecase.DeletePinUseCase
import com.fappslab.features.common.domain.usecase.GetFingerprintStateUseCase
import com.fappslab.features.common.domain.usecase.GetPinStateUseCase
import com.fappslab.features.common.domain.usecase.GetPinUseCase
import com.fappslab.features.common.domain.usecase.GetShufflePinStateUseCase
import com.fappslab.features.common.domain.usecase.SetPinUseCase

internal data class LockProvider(
    val getPinUseCase: GetPinUseCase,
    val setPinUseCase: SetPinUseCase,
    val deletePinUseCase: DeletePinUseCase,
    val getPinStateUseCase: GetPinStateUseCase,
    val getShufflePinStateUseCase: GetShufflePinStateUseCase,
    val getFingerprintStateUseCase: GetFingerprintStateUseCase
)
