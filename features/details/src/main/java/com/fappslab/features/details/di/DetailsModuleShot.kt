package com.fappslab.features.details.di

import com.fappslab.features.common.navigation.DetailsNavigation
import com.fappslab.features.details.navigation.DetailsNavigationImpl
import com.fappslab.seedcake.libraries.arch.koin.koinshot.KoinShot
import org.koin.core.module.Module
import org.koin.dsl.module

internal class DetailsModuleShot : KoinShot() {

    override val additionalModule: Module = module {
        factory<DetailsNavigation> { DetailsNavigationImpl() }
    }
}
