package com.fappslab.features.encrypt.main.di

import com.fappslab.features.common.navigation.EncryptNavigation
import com.fappslab.features.encrypt.main.navigation.EncryptNavigationImpl
import com.fappslab.seedcake.libraries.arch.koin.koinshot.KoinShot
import org.koin.core.module.Module
import org.koin.dsl.module

internal class EncryptModuleShot : KoinShot() {

    override val additionalModule: Module = module {
        factory<EncryptNavigation> { EncryptNavigationImpl() }
    }
}
