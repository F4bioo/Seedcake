package com.fappslab.features.details.presentation.viewmodel

internal sealed class DetailsViewAction {
    data object CloseSeed : DetailsViewAction()
    data object FinishView : DetailsViewAction()
    data object Validation : DetailsViewAction()
    data object RequestPermission : DetailsViewAction()
    data object GrantedPermission : DetailsViewAction()
    data object OpenAppSettings : DetailsViewAction()
    data object WhatSeeing : DetailsViewAction()
    data class Copy(val encryptedSeed: String) : DetailsViewAction()
    data class SaveToGalleryResult(val message: Int) : DetailsViewAction()
    data class UnlockedSeed(val params: UnlockParams) : DetailsViewAction()
}

internal data class UnlockParams(
    val decryptedSeed: String,
    val coloredSeed: List<Pair<String, String>>
)
