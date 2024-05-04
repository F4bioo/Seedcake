package com.fappslab.features.encrypt.secret.di

import com.fappslab.features.encrypt.secret.domain.usecase.PassphraseGeneratorUseCase
import com.fappslab.features.encrypt.secret.presentation.viewmodel.passphrase.GeneratorViewModel
import com.fappslab.features.encrypt.secret.presentation.viewmodel.secret.SecretViewModel
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object SecretModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel {
            SecretViewModel()
        }
        viewModel {
            GeneratorViewModel(PassphraseGeneratorUseCase())
        }
    }
}
