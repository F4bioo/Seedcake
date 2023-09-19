package com.fappslab.features.encrypt.result.di

import com.fappslab.features.common.domain.usecase.SetSeedUseCase
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs
import com.fappslab.features.encrypt.result.presentation.viewmodel.ResultViewModel
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object ResultModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel { (args: ResultArgs) ->
            ResultViewModel(
                args = args,
                setSeedUseCase = SetSeedUseCase(repository = get())
            )
        }
    }
}
