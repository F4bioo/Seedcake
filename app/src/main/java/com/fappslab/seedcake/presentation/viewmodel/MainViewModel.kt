package com.fappslab.seedcake.presentation.viewmodel

import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel

class MainViewModel : ViewModel<MainViewState, MainViewAction>(MainViewState()) {

    fun onBackPressed(isHomeFragment: Boolean) {
        val action = when {
            isHomeFragment -> MainViewAction.FinishView
            else -> MainViewAction.BackPressed
        }
        onAction { action }
    }

    fun onAboutItem() {
        onAction { MainViewAction.About }
    }
}
