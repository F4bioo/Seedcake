package com.fappslab.features.data.local.model.extension

import com.fappslab.features.data.local.model.SeedEntity
import com.fappslab.features.common.domain.model.Seed

internal fun SeedEntity.toSeed(): Seed =
    Seed(
        id = id,
        alias = alias,
        date = date,
        unreadableSeedPhrase = encryptedSeed
    )

internal fun Seed.toSeedEntity(): SeedEntity =
    SeedEntity(
        id = id,
        alias = alias,
        date = date,
        encryptedSeed = unreadableSeedPhrase
    )

internal fun List<SeedEntity>.toSeeds(): List<Seed> =
    map { it.toSeed() }
