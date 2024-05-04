package com.fappslab.features.home.presentation.model.extension

import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.navigation.DetailsArgs

internal fun Seed.toDetailsArgs(): DetailsArgs =
    DetailsArgs(
        id = id,
        alias = alias,
        date = date,
        unreadableSeedPhrase = unreadableSeedPhrase
    )
