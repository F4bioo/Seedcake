package com.fappslab.features.encrypt.seed.presentation.viewmodel.seed

import com.fappslab.features.encrypt.main.presentation.model.ProgressType.Progress3
import com.fappslab.features.encrypt.seed.presentation.component.InputType
import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmType
import com.fappslab.seedcake.features.encrypt.R
import com.fappslab.seedcake.libraries.extension.filterNotBlank

internal data class SeedViewState(
    val shouldEnableGroupTypes: Boolean = true,
    val shouldShowProgressDialog: Boolean = false,
    val shouldShowLockSeedErrorDialog: Boolean = false,
    val shouldShowAlgorithmPage: Boolean = false,
    val inputWords: List<String> = emptyList(),
    val progress: Int = Progress3.ordinal,
    val inputType: InputType = InputType.TWELVE,
    val algorithmType: AlgorithmType = AlgorithmType.recommendedType,
    val dialogErrorPair: Pair<Int, String?> = Pair(R.string.unknown_error, null),
) {

    fun dialogError(dialogErrorPair: Pair<Int, String?>) = copy(
        shouldShowLockSeedErrorDialog = true,
        dialogErrorPair = dialogErrorPair
    )

    fun wordChanged(inputWords: List<String>) = copy(
        shouldEnableGroupTypes = inputWords.isEnable(),
        inputWords = inputWords
    )

    private fun List<String>.isEnable(): Boolean =
        filterNotBlank().size <= InputType.TWELVE.count
}
