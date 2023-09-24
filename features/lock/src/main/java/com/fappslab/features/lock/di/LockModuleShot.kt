package com.fappslab.features.lock.di

import com.fappslab.features.common.navigation.LockNavigation
import com.fappslab.features.lock.navigation.LockNavigationImpl
import com.fappslab.seedcake.libraries.arch.koin.koinshot.KoinShot
import org.koin.core.module.Module
import org.koin.dsl.module

internal class LockModuleShot : KoinShot() {

    override val additionalModule: Module = module {
        factory<LockNavigation> { LockNavigationImpl() }
    }
}
