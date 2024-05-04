package com.fappslab.seedcake.di

import com.fappslab.features.common.domain.usecase.GetPinStateUseCase
import com.fappslab.features.common.domain.usecase.GetScreenShieldStateUseCase
import com.fappslab.features.common.domain.usecase.GetShufflePinStateUseCase
import com.fappslab.seedcake.libraries.arch.koin.koinshot.KoinShot
import org.koin.core.module.Module
import org.koin.dsl.module

internal class AppModuleShot : KoinShot() {

    override val domainModule: Module = module {
        factory { GetScreenShieldStateUseCase(repository = get()) }
        factory { GetPinStateUseCase(repository = get()) }
        factory { GetShufflePinStateUseCase(repository = get())}
    }
}
