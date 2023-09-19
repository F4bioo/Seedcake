package com.fappslab.features.details.di

import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.common.navigation.DetailsArgs
import com.fappslab.libraries.test.koin.KoinModuleTest
import com.fappslab.libraries.test.rules.CoroutineTestRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class DetailsModuleLoadTest : KoinModuleTest(DetailsModuleLoad) {

    @get:Rule
    override val coroutineTestRule = CoroutineTestRule()

    override val mockedModules = module {
        factory<StorageRepository> { mockk(relaxed = true) }
        factory<ObfuscationRepository> { mockk(relaxed = true) }
    }

    @Test
    fun `checkModules Should Koin provides dependencies When invoke DetailsModuleLoad`() {
        checkModules {
            withInstance(detailsArgsStub())
        }
    }

    private fun detailsArgsStub() =
        DetailsArgs(
            id = 0,
            alias = "Trezor wallet",
            date = "10-Aug-2023, 02:15 PM",
            encryptedSeed = "UMDJ3sMCSF9q42IGeFmkSrUhpckbUiVPZR06eBGEApI6uvYKFdADW6i…"
        )
}
