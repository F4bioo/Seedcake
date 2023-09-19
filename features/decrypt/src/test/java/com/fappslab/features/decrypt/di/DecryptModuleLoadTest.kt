package com.fappslab.features.decrypt.di

import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.decrypt.presentation.model.PageType
import com.fappslab.libraries.test.koin.KoinModuleTest
import com.fappslab.libraries.test.rules.CoroutineTestRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class DecryptModuleLoadTest : KoinModuleTest(DecryptModuleLoad) {

    @get:Rule
    override val coroutineTestRule = CoroutineTestRule()

    override val mockedModules = module {
        factory<ObfuscationRepository> { mockk(relaxed = true) }
    }

    @Test
    fun `checkModules Should Koin provides dependencies When invoke DecryptModuleLoad`() {
        checkModules {
            withInstance(PageType.EncryptedSeed)
        }
    }
}
