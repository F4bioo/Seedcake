package com.fappslab.features.data.local.di

import android.content.Context
import android.content.SharedPreferences
import com.fappslab.features.data.local.db.SeedcakeDao
import com.fappslab.libraries.security.bip39colors.BIP39Colors
import com.fappslab.libraries.security.bip39words.Bip39Words
import com.fappslab.libraries.security.cryptography.CryptoManager
import com.fappslab.libraries.test.koin.KoinModuleTest
import com.fappslab.libraries.test.rules.CoroutineTestRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class LocalModuleShotTest : KoinModuleTest(LocalModuleShot) {

    @get:Rule
    override val coroutineTestRule = CoroutineTestRule()

    override val mockedModules = module {
        factory<Context> { mockk(relaxed = true) }
        factory<CryptoManager> { mockk(relaxed = true) }
        factory<Bip39Words> { mockk(relaxed = true) }
        factory<BIP39Colors> { mockk(relaxed = true) }
        factory<SeedcakeDao> { mockk(relaxed = true) }
        factory<SharedPreferences>(qualifier = SecurePrefsQualifier) { mockk(relaxed = true) }
        factory<SharedPreferences>(qualifier = SimplePrefsQualifier) { mockk(relaxed = true) }
    }

    @Test
    fun `checkModules Should Koin provides dependencies When invoke LocalModuleShot`() {
        checkModules()
    }
}
