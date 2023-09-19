package com.fappslab.seedcake.libraries.arch.koin.koinlazy

import com.fappslab.seedcake.libraries.arch.koin.koinload.KoinLoad
import org.koin.android.scope.AndroidScopeComponent

interface KoinLazy : AndroidScopeComponent {
    val koinLoad: KoinLoad
}
