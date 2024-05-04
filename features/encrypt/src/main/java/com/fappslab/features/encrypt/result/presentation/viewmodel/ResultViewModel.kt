package com.fappslab.features.encrypt.result.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.fappslab.features.common.domain.usecase.SetSeedPhraseUseCase
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs
import com.fappslab.features.encrypt.result.presentation.model.extension.toSeed
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import com.fappslab.seedcake.libraries.design.pluto.activity.qrcode.creator.PlutoQrcodeCreator
import kotlinx.coroutines.launch

internal class ResultViewModel(
    private val args: ResultArgs,
    private val setSeedPhraseUseCase: SetSeedPhraseUseCase,
) : ViewModel<ResultViewState, ResultViewAction>(ResultViewState(args)) {

    init {
        createQrcode()
    }

    private fun createQrcode() {
        val qrcode = PlutoQrcodeCreator.create(args.encryptedSeed)
        onState { it.copy(encryptedSeedBitmap = qrcode) }
    }

    fun onSave() {
        viewModelScope.launch {
            onState { it.copy(shouldEnableSaveButton = false) }
                .runCatching { setSeedPhraseUseCase(args.toSeed()) }
                .onFailure { saveFailure() }
                .onSuccess { saveSuccess() }
        }
    }

    private fun saveFailure() {
        onState { it.saveFailure() }
    }

    private fun saveSuccess() {
        onAction { ResultViewAction.SaveSuccess }
    }

    fun onFullEncryptedSeedVisibility(shouldShow: Boolean = true) {
        onState { it.copy(shouldShowFullEncryptedSeedModal = shouldShow) }
    }

    fun onCopy() {
        onAction { ResultViewAction.Copy(args.encryptedSeed) }
    }

    fun onWhatSeeing() {
        onAction { ResultViewAction.WhatSeeing }
    }

    fun onFinishView() {
        onSaveErrorVisibilityModal()
        onAction { ResultViewAction.FinishView }
    }

    fun onTryAgain() {
        onSaveErrorVisibilityModal()
        onSave()
    }

    fun onSaveErrorVisibilityModal(shouldShow: Boolean = false) {
        onState { it.copy(shouldShowSaveErrorModal = shouldShow) }
    }

    fun onBackPressed() {
        onAction { ResultViewAction.BackPressed }
    }
}
