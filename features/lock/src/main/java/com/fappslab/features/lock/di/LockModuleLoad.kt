package com.fappslab.features.lock.di

import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.common.domain.usecase.DeletePinUseCase
import com.fappslab.features.common.domain.usecase.GetFingerprintStateUseCase
import com.fappslab.features.common.domain.usecase.GetPinUseCase
import com.fappslab.features.common.domain.usecase.SetPinUseCase
import com.fappslab.features.common.navigation.ScreenTypeArgs
import com.fappslab.features.lock.presentation.viewmodel.LockViewModel
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object LockModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel { (args: ScreenTypeArgs) ->
            val repository = get<StorageRepository>()
            LockViewModel(
                args = args,
                getPinUseCase = GetPinUseCase(repository),
                setPinUseCase = SetPinUseCase(repository),
                deletePinUseCase = DeletePinUseCase(repository),
                getFingerprintStateUseCase = GetFingerprintStateUseCase(repository)
            )
        }
    }
}
