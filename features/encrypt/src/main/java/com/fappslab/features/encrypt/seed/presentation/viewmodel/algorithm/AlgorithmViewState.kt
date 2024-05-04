package com.fappslab.features.encrypt.seed.presentation.viewmodel.algorithm

import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmItem
import com.fappslab.features.encrypt.seed.presentation.model.AlgorithmType
import com.fappslab.seedcake.features.encrypt.R

internal data class AlgorithmViewState(
    val algorithms: List<AlgorithmItem> = listOf(
        AlgorithmItem(AlgorithmType.AES_GCM_NOPADDING),
        AlgorithmItem(AlgorithmType.CHACHA20_POLY1305),
        AlgorithmItem(AlgorithmType.AES_CTR_NOPADDING),
        AlgorithmItem(AlgorithmType.AES_CBC_PKCS5PADDING)
    )
) {

    fun cardClick(algorithmItem: AlgorithmItem): AlgorithmViewState {
        val updatedAlgorithms = algorithms.map { item ->
            if (item.type == algorithmItem.type) {
                item.expanded(!item.isExpanded)
            } else item
        }
        return copy(algorithms = updatedAlgorithms)
    }

    private fun AlgorithmItem.expanded(shouldExpandDetails: Boolean) = copy(
        isExpanded = shouldExpandDetails,
        expandableIconRes = if (shouldExpandDetails) {
            R.drawable.plu_ic_expand_less
        } else R.drawable.plu_ic_expand_more
    )
}
