package com.fappslab.features.encrypt.disclaimer.di

import com.fappslab.libraries.test.koin.KoinModuleTest
import com.fappslab.libraries.test.rules.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class DisclaimerModuleLoadTest : KoinModuleTest(DisclaimerModuleLoad) {

    @get:Rule
    override val coroutineTestRule = CoroutineTestRule()

    @Test
    fun `checkModules Should Koin provides dependencies When invoke DisclaimerModuleLoad`() {
        checkModules()
    }
}
