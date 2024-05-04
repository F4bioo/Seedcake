package com.fappslab.features.preferences.di

import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.common.domain.usecase.GetFingerprintStateUseCase
import com.fappslab.features.common.domain.usecase.GetPinUseCase
import com.fappslab.features.preferences.presentation.viewmodel.PreferencesViewModel
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object PreferencesModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel {
            val repository = get<StorageRepository>()
            PreferencesViewModel(
                getPinUseCase = GetPinUseCase(repository),
                getPinStateUseCase = get(),
                getShufflePinStateUseCase = get(),
                getFingerprintStateUseCase = GetFingerprintStateUseCase(repository),
            )
        }
    }
}
