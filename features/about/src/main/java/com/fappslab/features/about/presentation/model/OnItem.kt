package com.fappslab.features.about.presentation.model

internal sealed class OnItem {
    object Close : OnItem()
    object Header : OnItem()
    data class Item(val url: String) : OnItem()
}
