package com.fappslab.features.decrypt.di

import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.common.domain.usecase.DecodeSeedPhraseColorUseCase
import com.fappslab.features.common.domain.usecase.DecryptSeedPhraseUseCase
import com.fappslab.features.decrypt.presentation.model.PageType
import com.fappslab.features.decrypt.presentation.viewmodel.DecryptViewModel
import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal object DecryptModuleLoad : KoinLoad() {

    override val presentationModule: Module = module {
        viewModel { (pageType: PageType) ->
            val repository = get<ObfuscationRepository>()
            DecryptViewModel(
                pageType = pageType,
                decryptSeedPhraseUseCase = DecryptSeedPhraseUseCase(repository),
                decodeSeedPhraseColorUseCase = DecodeSeedPhraseColorUseCase(repository)
            )
        }
    }
}
