package com.fappslab.libraries.test.koin

import androidx.annotation.VisibleForTesting
import com.fappslab.libraries.test.rules.CoroutineTestRule
import com.fappslab.seedcake.libraries.arch.koin.KoinModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.ParametersBinding
import org.koin.test.check.checkKoinModules
import java.lang.reflect.Modifier

@VisibleForTesting(otherwise = Modifier.PRIVATE)
@ExperimentalCoroutinesApi
abstract class KoinModuleTest(private val koinModules: KoinModules) : KoinTest {

    abstract val coroutineTestRule: CoroutineTestRule

    protected open val mockedModules: Module = module {}

    protected open fun KoinModuleTest.checkModules(checkBlock: ParametersBinding.() -> Unit = {}) {
        checkKoinModules(modules = koinModules.modules + mockedModules, parameters = checkBlock)
    }
}
