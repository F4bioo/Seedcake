package com.fappslab.features.home.presentation.viewmodel

import com.fappslab.features.common.navigation.DetailsArgs

internal sealed class HomeViewAction {
    data object Add : HomeViewAction()
    data object Resume : HomeViewAction()
    data class AdapterItem(val args: DetailsArgs) : HomeViewAction()
}
