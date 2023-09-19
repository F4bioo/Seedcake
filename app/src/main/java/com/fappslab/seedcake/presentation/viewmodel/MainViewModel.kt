package com.fappslab.seedcake.presentation.viewmodel

import com.fappslab.features.common.domain.usecase.GetPinStateUseCase
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel

class MainViewModel(
    private val getPinStateUseCase: GetPinStateUseCase
) : ViewModel<MainViewState, MainViewAction>(MainViewState()) {

    init {
        onShowLockScreen()
    }

    private fun onShowLockScreen() {
        if (getPinStateUseCase()) {
            onAction { MainViewAction.Protected }
        }
    }

    fun onBackPressed(isHomeFragment: Boolean) {
        val action = when {
            isHomeFragment && getPinStateUseCase() -> MainViewAction.Protected
            isHomeFragment -> MainViewAction.FinishView
            else -> MainViewAction.BackPressed
        }
        onAction { action }
    }

    fun onAboutItem() {
        onAction { MainViewAction.About }
    }
}
