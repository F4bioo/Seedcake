package com.fappslab.features.encrypt.result.presentation.model.extension

import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs
import com.fappslab.seedcake.libraries.arch.dateformat.toRawDateTime
import org.joda.time.DateTime

internal fun ResultArgs.toSeed(): Seed {
    val now = DateTime.now()
    return Seed(
        alias = alias,
        date = now.toRawDateTime(),
        encryptedSeed = encryptedSeed
    )
}
