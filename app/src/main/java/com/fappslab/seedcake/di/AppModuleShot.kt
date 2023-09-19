package com.fappslab.seedcake.di

import com.fappslab.features.common.domain.usecase.GetScreenShieldStateUseCase
import com.fappslab.seedcake.libraries.arch.koin.koinshot.KoinShot
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModuleShot : KoinShot() {

    override val domainModule: Module = module {
        factory { GetScreenShieldStateUseCase(repository = get()) }
    }
}
