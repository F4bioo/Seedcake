package com.fappslab.seedcake.presentation.viewmodel

sealed class MainViewAction {
    data object FinishView : MainViewAction()
    data object BackPressed : MainViewAction()
    data object About : MainViewAction()
}
