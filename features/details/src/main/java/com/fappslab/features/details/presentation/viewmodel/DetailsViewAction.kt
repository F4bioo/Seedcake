package com.fappslab.features.details.presentation.viewmodel

internal sealed class DetailsViewAction {
    object FinishView : DetailsViewAction()
    object Validation : DetailsViewAction()
    object RequestPermission : DetailsViewAction()
    object GrantedPermission : DetailsViewAction()
    object OpenAppSettings : DetailsViewAction()
    object WhatSeeing : DetailsViewAction()
    data class Copy(val encryptedSeed: String) : DetailsViewAction()
    data class SaveToGalleryResult(val message: Int) : DetailsViewAction()
    data class Decrypted(val params: DecryptParams) : DetailsViewAction()
}

internal data class DecryptParams(
    val decryptedSeed: String,
    val coloredSeed: List<Pair<String, String>>
)
