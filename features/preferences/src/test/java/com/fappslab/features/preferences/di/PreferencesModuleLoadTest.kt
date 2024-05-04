package com.fappslab.features.preferences.di

import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.common.domain.usecase.GetPinStateUseCase
import com.fappslab.features.common.domain.usecase.GetShufflePinStateUseCase
import com.fappslab.libraries.test.koin.KoinModuleTest
import com.fappslab.libraries.test.rules.CoroutineTestRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class PreferencesModuleLoadTest : KoinModuleTest(PreferencesModuleLoad) {

    @get:Rule
    override val coroutineTestRule = CoroutineTestRule()

    override val mockedModules = module {
        factory<StorageRepository> { mockk(relaxed = true) }
        factory<GetPinStateUseCase> { mockk(relaxed = true) }
        factory<GetShufflePinStateUseCase> { mockk(relaxed = true) }
    }

    @Test
    fun `checkModules Should Koin provides dependencies When invoke PreferencesModuleLoad`() {
        checkModules()
    }
}
