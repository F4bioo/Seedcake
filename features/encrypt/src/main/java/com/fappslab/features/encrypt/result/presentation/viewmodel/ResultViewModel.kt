package com.fappslab.features.encrypt.result.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.fappslab.features.common.domain.usecase.SetSeedUseCase
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs
import com.fappslab.features.encrypt.result.presentation.model.extension.toSeed
import com.fappslab.libraries.logger.Logger
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel
import com.fappslab.seedcake.libraries.design.pluto.activity.qrcode.creator.PlutoQrcodeCreator
import kotlinx.coroutines.launch

internal class ResultViewModel(
    private val args: ResultArgs,
    private val setSeedUseCase: SetSeedUseCase,
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
            onState { it.saveStart() }
                .runCatching { setSeedUseCase(args.toSeed()) }
                .onFailure { saveFailure(cause = it) }
                .onSuccess { saveSuccess() }
        }
    }

    private fun saveFailure(cause: Throwable) {
        Logger.log.e(cause.message)
        onState { it.saveFailure() }
    }

    private fun saveSuccess() {
        // TODO implement success action
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

    fun onClose() {
        onAction { ResultViewAction.FinishView }
    }
}
