package com.fappslab.features.encrypt.disclaimer.presentation.viewmodel

import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel

internal class DisclaimerViewModel :
    ViewModel<DisclaimerViewState, DisclaimerViewAction>(DisclaimerViewState()) {

    fun onConfirm(isChecked: Boolean) {
        onState { it.copy(isConfirmChecked = isChecked) }
    }

    fun onContinue() {
        if (state.value.isConfirmChecked) {
            onAction { DisclaimerViewAction.Continue }
        } else onState { it.copy(shouldShowError = true) }
    }

    fun onDismissError() {
        onState { it.copy(shouldShowError = false) }
    }
}
