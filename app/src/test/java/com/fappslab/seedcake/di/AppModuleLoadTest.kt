package com.fappslab.seedcake.di

import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.libraries.test.koin.KoinModuleTest
import com.fappslab.libraries.test.rules.CoroutineTestRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

@ExperimentalCoroutinesApi
class AppModuleLoadTest : KoinModuleTest(AppModuleLoad) {

    @get:Rule
    override val coroutineTestRule = CoroutineTestRule()

    override val mockedModules = module {
        factory<StorageRepository> { mockk(relaxed = true) }
    }

    @Test
    fun `checkModules Should Koin provides dependencies When invoke AppModuleLoad`() {
        checkModules()
    }
}
