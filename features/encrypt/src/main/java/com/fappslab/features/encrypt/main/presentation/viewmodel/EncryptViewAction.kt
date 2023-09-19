package com.fappslab.features.encrypt.main.presentation.viewmodel

import com.fappslab.features.encrypt.result.presentation.model.ResultArgs

internal sealed class EncryptViewAction {
    data class Result(val args: ResultArgs) : EncryptViewAction()
}
