package com.fappslab.seedcake.presentation.viewmodel

sealed class MainViewAction {
    object Protected : MainViewAction()
    object FinishView : MainViewAction()
    object BackPressed : MainViewAction()
    object About : MainViewAction()
}
