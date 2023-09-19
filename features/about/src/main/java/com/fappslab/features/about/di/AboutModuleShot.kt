package com.fappslab.features.about.di

import com.fappslab.features.about.navigation.AboutNavigationImpl
import com.fappslab.features.common.navigation.AboutNavigation
import com.fappslab.seedcake.libraries.arch.koin.koinshot.KoinShot
import org.koin.core.module.Module
import org.koin.dsl.module

internal object AboutModuleShot : KoinShot() {

    override val additionalModule: Module = module {
        factory<AboutNavigation> { AboutNavigationImpl() }
    }
}
