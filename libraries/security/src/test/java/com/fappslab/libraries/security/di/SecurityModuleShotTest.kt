package com.fappslab.libraries.security.di

import android.content.Context
import com.fappslab.libraries.security.bip39words.Bip39Words
import com.fappslab.libraries.test.koin.KoinModuleTest
import com.fappslab.libraries.test.rules.CoroutineTestRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class SecurityModuleShotTest : KoinModuleTest(SecurityModuleShot) {

    @get:Rule
    override val coroutineTestRule = CoroutineTestRule()

    override val mockedModules = module {
        factory<Context> { mockk(relaxed = true) }
        factory<Bip39Words> { mockk(relaxed = true) }
    }

    @Test
    fun `checkModules Should Koin provides dependencies When invoke SecurityModuleShot`() {
        checkModules()
    }
}
