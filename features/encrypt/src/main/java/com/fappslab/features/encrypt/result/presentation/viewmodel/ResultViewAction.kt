package com.fappslab.features.encrypt.result.presentation.viewmodel

internal sealed class ResultViewAction {
    object FinishView : ResultViewAction()
    object WhatSeeing : ResultViewAction()
    data class Copy(val encryptedSeed: String) : ResultViewAction()
}
