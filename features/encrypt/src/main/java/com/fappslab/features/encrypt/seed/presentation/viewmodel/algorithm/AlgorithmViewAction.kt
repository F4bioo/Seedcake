package com.fappslab.features.encrypt.seed.presentation.viewmodel.algorithm

import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmType

internal sealed class AlgorithmViewAction {
    data object CloseButton : AlgorithmViewAction()
    data class UseButton(val algorithmType: AlgorithmType) : AlgorithmViewAction()
}
