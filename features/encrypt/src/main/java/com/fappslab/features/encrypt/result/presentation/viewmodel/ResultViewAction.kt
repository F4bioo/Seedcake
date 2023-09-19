package com.fappslab.features.encrypt.result.presentation.viewmodel

internal sealed class ResultViewAction {
    object FinishView : ResultViewAction()
    data class Copy(val encryptedSeed: String) : ResultViewAction()
}
