package com.fappslab.seedcake.libraries.arch.viewmodel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModel as LifecycleViewModel

abstract class ViewModel<S, A>(initialState: S) : LifecycleViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _action = Channel<A>(Channel.CONFLATED)
    val action: Flow<A> = _action.receiveAsFlow()

    protected fun onState(stateBlock: (S) -> S) {
        _state.update { stateBlock(it) }
    }

    protected fun onAction(actionBlock: () -> A) {
        _action.trySend(actionBlock())
    }

    override fun onCleared() {
        super.onCleared()
        _action.close()
    }
}
