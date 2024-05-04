package com.fappslab.features.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.domain.usecase.GetSeedPhrasesUseCase
import com.fappslab.features.home.presentation.model.extension.toDetailsArgs
import com.fappslab.libraries.logger.Logger
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal class HomeViewModel(
    private val getSeedPhrasesUseCase: GetSeedPhrasesUseCase
) : ViewModel<HomeViewState, HomeViewAction>(HomeViewState()) {

    init {
        getSeeds()
    }

    private fun getSeeds() {
        getSeedPhrasesUseCase()
            .onStart { onState { it.copy(shouldShowLoading = true) } }
            .catch { Logger.log.e(it.message) }
            .onCompletion { onState { it.copy(shouldShowLoading = false) } }
            .onEach { seeds -> onState { it.submitList(seeds = seeds) } }
            .launchIn(viewModelScope)
    }

    fun onAdapterItem(seed: Seed) {
        onAction { HomeViewAction.AdapterItem(seed.toDetailsArgs()) }
    }

    fun onAddButton() {
        onAction { HomeViewAction.Add }
    }

    fun onResume() {
        onAction { HomeViewAction.Resume }
    }
}
