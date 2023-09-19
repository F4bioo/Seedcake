package com.fappslab.features.about.presentation.viewmodel

import com.fappslab.features.about.presentation.model.Header
import com.fappslab.features.about.presentation.model.Item

internal data class AboutViewState(
    val header: Header = Header(),
    val items: List<Item>
)
