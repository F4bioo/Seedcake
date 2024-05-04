package com.fappslab.features.encrypt.seed.di

import com.fappslab.features.common.domain.usecase.EncryptSeedPhraseUseCase
import com.fappslab.features.encrypt.seed.presentation.viewmodel.algorithm.AlgorithmViewModel
import com.fappslab.features.encrypt.seed.presentation.viewmodel.seed.SeedViewModel
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object SeedModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel {
            SeedViewModel(
                encryptSeedPhraseUseCase = EncryptSeedPhraseUseCase(repository = get())
            )
        }

        viewModel {
            AlgorithmViewModel()
        }
    }
}
