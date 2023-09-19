package com.fappslab.features.about.presentation.viewmodel

import com.fappslab.features.about.presentation.model.Item
import com.fappslab.features.about.presentation.model.OnItem
import com.fappslab.seedcake.libraries.arch.viewmodel.ViewModel

internal class AboutViewModel(
    items: List<Item>
) : ViewModel<AboutViewState, AboutViewAction>(AboutViewState(items = items)) {

    fun onFooterClicked(releaseData: CharSequence) {
        onAction { AboutViewAction.FooterClicked(releaseData.toString()) }
    }

    fun onAdapterItem(item: OnItem) {
        val action = when (item) {
            OnItem.Close -> AboutViewAction.CloseClicked
            OnItem.Header -> AboutViewAction.RateClicked
            is OnItem.Item -> AboutViewAction.ItemClicked(item.url)
        }
        onAction { action }
    }
}
