package com.fappslab.features.encrypt.result.presentation.viewmodel

internal sealed class ResultViewAction {
    data object BackPressed : ResultViewAction()
    data object FinishView : ResultViewAction()
    data object WhatSeeing : ResultViewAction()
    data object SaveSuccess : ResultViewAction()
    data class Copy(val encryptedSeed: String) : ResultViewAction()
}
