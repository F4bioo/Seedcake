package com.fappslab.seedcake.di

import com.fappslab.features.common.domain.usecase.GetPinStateUseCase
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import com.fappslab.seedcake.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel {
            MainViewModel(
                getPinStateUseCase = GetPinStateUseCase(repository = get()),
            )
        }
    }
}
