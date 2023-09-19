package com.fappslab.features.decrypt.presentation.viewmodel

internal sealed class DecryptViewAction {
    object Validation : DecryptViewAction()
    object ClearInput : DecryptViewAction()
    object RequestPermission : DecryptViewAction()
    object GrantedPermission : DecryptViewAction()
    object OpenAppSettings : DecryptViewAction()
    data class ScannerResult(val unreadable: String) : DecryptViewAction()
    data class UnlockedSeed(val readableSeed: String) : DecryptViewAction()
}
