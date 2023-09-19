package com.fappslab.seedcake.libraries.arch.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

inline fun <reified S, reified A> LifecycleOwner.onViewState(
    viewModel: ViewModel<S, A>,
    crossinline state: (S) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.state.collect { state(it) }
        }
    }
}

inline fun <reified S, reified A> LifecycleOwner.onViewAction(
    viewModel: ViewModel<S, A>,
    crossinline action: (A) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.action.collect { action(it) }
        }
    }
}
