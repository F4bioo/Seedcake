package com.fappslab.features.details.di

import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.common.domain.usecase.DecryptSeedUseCase
import com.fappslab.features.common.domain.usecase.DeleteSeedUseCase
import com.fappslab.features.common.domain.usecase.EncodeSeedColorUseCase
import com.fappslab.features.common.domain.usecase.SetSeedUseCase
import com.fappslab.features.common.navigation.DetailsArgs
import com.fappslab.features.details.presentation.viewmodel.DetailsViewModel
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object DetailsModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel { (args: DetailsArgs) ->
            val storageRepository = get<StorageRepository>()
            val obfuscationRepository = get<ObfuscationRepository>()
            DetailsViewModel(
                args = args,
                setSeedUseCase = SetSeedUseCase(storageRepository),
                deleteSeedUseCase = DeleteSeedUseCase(storageRepository),
                decryptSeedUseCase = DecryptSeedUseCase(obfuscationRepository),
                encodeSeedColorUseCase = EncodeSeedColorUseCase(obfuscationRepository)
            )
        }
    }
}
