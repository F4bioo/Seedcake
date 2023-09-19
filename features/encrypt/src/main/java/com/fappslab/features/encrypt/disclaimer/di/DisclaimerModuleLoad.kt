package com.fappslab.features.encrypt.disclaimer.di

import com.fappslab.features.encrypt.disclaimer.presentation.viewmodel.DisclaimerViewModel
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object DisclaimerModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel {
            DisclaimerViewModel()
        }
    }
}
