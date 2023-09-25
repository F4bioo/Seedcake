package com.fappslab.features.encrypt.main.di

import com.fappslab.features.common.domain.usecase.EncryptSeedUseCase
import com.fappslab.features.encrypt.main.presentation.viewmodel.EncryptViewModel
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object EncryptModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel {
            EncryptViewModel(
                encryptSeedUseCase = EncryptSeedUseCase(repository = get())
            )
        }
    }
}
