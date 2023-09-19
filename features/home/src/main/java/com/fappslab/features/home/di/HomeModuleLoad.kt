package com.fappslab.features.home.di

import com.fappslab.features.common.domain.usecase.GetSeedsUseCase
import com.fappslab.features.home.presentation.viewmodel.HomeViewModel
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object HomeModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel {
            HomeViewModel(
                getSeedsUseCase = GetSeedsUseCase(repository = get())
            )
        }
    }
}
