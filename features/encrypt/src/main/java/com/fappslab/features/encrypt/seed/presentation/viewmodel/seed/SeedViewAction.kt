package com.fappslab.features.encrypt.seed.presentation.viewmodel.seed

import com.fappslab.features.encrypt.result.presentation.model.ResultArgs

internal sealed class SeedViewAction {
    data object NextInput : SeedViewAction()
    data object BackspaceKey : SeedViewAction()
    data class LetterKey(val letter: String) : SeedViewAction()
    data class Suggestion(val suggestion: String) : SeedViewAction()
    data class Result(val args: ResultArgs) : SeedViewAction()
}
