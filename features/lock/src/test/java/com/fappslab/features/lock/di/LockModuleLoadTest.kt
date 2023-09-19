package com.fappslab.features.lock.di

import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.common.navigation.ScreenTypeArgs
import com.fappslab.libraries.test.koin.KoinModuleTest
import com.fappslab.libraries.test.rules.CoroutineTestRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class LockModuleLoadTest : KoinModuleTest(LockModuleLoad) {

    @get:Rule
    override val coroutineTestRule = CoroutineTestRule()

    override val mockedModules = module {
        factory<StorageRepository> { mockk(relaxed = true) }
    }

    @Test
    fun `checkModules Should Koin provides dependencies When invoke LockModuleLoad`() {
        checkModules {
            withInstance(ScreenTypeArgs.LockScreen)
        }
    }
}