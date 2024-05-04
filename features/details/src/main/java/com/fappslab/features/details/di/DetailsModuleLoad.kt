package com.fappslab.features.details.di

import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.common.domain.usecase.DecryptSeedPhraseUseCase
import com.fappslab.features.common.domain.usecase.DeleteSeedPhraseUseCase
import com.fappslab.features.common.domain.usecase.EncodeSeedPhraseColorUseCase
import com.fappslab.features.common.domain.usecase.SetSeedPhraseUseCase
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
                setSeedPhraseUseCase = SetSeedPhraseUseCase(storageRepository),
                deleteSeedPhraseUseCase = DeleteSeedPhraseUseCase(storageRepository),
                decryptSeedPhraseUseCase = DecryptSeedPhraseUseCase(obfuscationRepository),
                encodeSeedPhraseColorUseCase = EncodeSeedPhraseColorUseCase(obfuscationRepository)
            )
        }
    }
}
