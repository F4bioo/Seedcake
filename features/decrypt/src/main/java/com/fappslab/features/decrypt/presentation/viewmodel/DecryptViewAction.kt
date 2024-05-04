package com.fappslab.features.decrypt.presentation.viewmodel

internal sealed class DecryptViewAction {
    data object Validation : DecryptViewAction()
    data object ClearInput : DecryptViewAction()
    data object RequestPermission : DecryptViewAction()
    data object GrantedPermission : DecryptViewAction()
    data object OpenAppSettings : DecryptViewAction()
    data class ScannerResult(val unreadable: String) : DecryptViewAction()
    data class UnlockedSeed(val readableSeed: String) : DecryptViewAction()
}
