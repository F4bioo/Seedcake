package com.fappslab.features.about.presentation.viewmodel

internal sealed class AboutViewAction {
    object CloseClicked : AboutViewAction()
    object RateClicked : AboutViewAction()
    data class ItemClicked(val url: String) : AboutViewAction()
    data class FooterClicked(val releaseData: String) : AboutViewAction()
}
