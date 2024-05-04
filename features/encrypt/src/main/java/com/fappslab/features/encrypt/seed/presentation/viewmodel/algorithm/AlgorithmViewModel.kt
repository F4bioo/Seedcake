package com.fappslab.features.encrypt.seed.presentation.viewmodel.algorithm

import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmItem.OnItem
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel

internal class AlgorithmViewModel :
    ViewModel<AlgorithmViewState, AlgorithmViewAction>(AlgorithmViewState()) {

    fun onItemClicked(item: OnItem) {
        when (item) {
            is OnItem.Card -> onState { it.cardClick(item.algorithmItem) }
            is OnItem.Use -> onAction { AlgorithmViewAction.UseButton(item.algorithmType) }
        }
    }

    fun onCloseClicked() {
        onAction { AlgorithmViewAction.CloseButton }
    }
}
