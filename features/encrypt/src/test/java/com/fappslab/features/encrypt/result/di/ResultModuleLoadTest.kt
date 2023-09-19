package com.fappslab.features.encrypt.result.di

import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.encrypt.result.presentation.model.ResultArgs
import com.fappslab.libraries.test.koin.KoinModuleTest
import com.fappslab.libraries.test.rules.CoroutineTestRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class ResultModuleLoadTest : KoinModuleTest(ResultModuleLoad) {

    @get:Rule
    override val coroutineTestRule = CoroutineTestRule()

    override val mockedModules = module {
        factory<StorageRepository> { mockk(relaxed = true) }
    }

    @Test
    fun `checkModules Should Koin provides dependencies When invoke ResultModuleLoad`() {
        checkModules {
            withInstance(resultArgsStub())
        }
    }

    private fun resultArgsStub() =
        ResultArgs(
            alias = "Trezor wallet",
            encryptedSeed = "UMDJ3sMCSF9q42IGeFmkSrUhpckbUiVPZR06eBGEApI6uvYKFdADW6i…"
        )
}
