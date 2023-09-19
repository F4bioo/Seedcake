package com.fappslab.features.data.local.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.fappslab.features.common.domain.model.Seed
import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.data.local.db.client.SimplePreferences
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.InjectParams
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.createDataSource
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.setupDatabaseWithItem
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.setupSecurePreferencesWithItem
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.simplePreferencesWithItem
import com.fappslab.libraries.test.rules.LocalTestRule
import com.fappslab.seedcake.core.data.local.BuildConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class StorageRepositoryImplIntegrationTest {

    @get:Rule
    val localTestRule = LocalTestRule()

    private val context by lazy {
        ApplicationProvider.getApplicationContext<Context>()
    }
    private val simplePreferences by lazy {
        SimplePreferences(context, BuildConfig.SIMPLE_PREFS_FILE_NAME).create()
    }

    private lateinit var subject: StorageRepository

    @Before
    fun setUp() {
        subject = StorageRepositoryImpl(
            dataSource = createDataSource(
                params = InjectParams(
                    context = context,
                    database = localTestRule.createTestDatabase(),
                    simplePreferences = simplePreferences
                )
            )
        )
    }

    @Test
    fun setSeedPhrase_Should_StoreSeedPhrase_When_GivenSeed() = runTest {
        // Given
        val expectedResult = listOf(SeedcakeRepositoryImplFixtures.seedStub())

        // When
        subject.setSeedPhrase(SeedcakeRepositoryImplFixtures.seedStub())

        // Then
        val result = subject.getSeedPhrases()
        result.test {
            assertEquals(expectedResult, awaitItem())
        }
    }

    @Test
    fun deleteSeedPhrase_Should_RemoveSeedPhrase_When_GivenId() = runTest {
        // Given
        val expectedResult = emptyList<Seed>()
        subject.setupDatabaseWithItem()

        // When
        subject.deleteSeedPhrase(id = 1)

        // Then
        val result = subject.getSeedPhrases()
        result.test {
            assertEquals(expectedResult, awaitItem())
        }
    }

    @Test
    fun getSeedPhrases_Should_ReturnListOfSeedPhrases_When_DataIsPresentInDatabase() = runTest {
        // Given
        val expectedResult = listOf(SeedcakeRepositoryImplFixtures.seedStub())
        subject.setupDatabaseWithItem()

        // When
        val result = subject.getSeedPhrases()

        // Then
        result.test {
            assertEquals(expectedResult, awaitItem())
        }
    }

    @Test
    fun setPin_Should_StoreLockscreenPin_When_GivenPinValue() = runTest {
        // Given
        val expectedResult = "4819"

        // When
        subject.setPin("4819")

        // Then
        val result = subject.getPin()
        assertEquals(expectedResult, result)
    }

    @Test
    fun deletePin_Should_RemoveLockscreenPin_When_StoredPinExists() = runTest {
        // Given
        val expectedResult = null
        subject.setupSecurePreferencesWithItem()

        // When
        subject.deletePin()

        // Then
        val result = subject.getPin()
        assertEquals(expectedResult, result)
    }

    @Test
    fun getPinCheckBoxState_Should_ReturnTrue_When_CheckBoxIsChecked() = runTest {
        // Given
        val expectedResult = true
        simplePreferences.simplePreferencesWithItem(BuildConfig.SIMPLE_PREFS_PIN_KEY)

        // When
        val result = subject.getPinCheckBoxState()

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun getFingerprintCheckBoxState_Should_ReturnTrue_When_CheckBoxIsChecked() = runTest {
        // Given
        val expectedResult = true
        simplePreferences.simplePreferencesWithItem(BuildConfig.SIMPLE_PREFS_FINGERPRINT_KEY)

        // When
        val result = subject.getFingerprintCheckBoxState()

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun getScreenShieldCheckBoxState_Should_ReturnTrue_When_CheckBoxIsChecked() = runTest {
        // Given
        val expectedResult = true
        simplePreferences.simplePreferencesWithItem(BuildConfig.SIMPLE_PREFS_SCREEN_SHIELD)

        // When
        val result = subject.getScreenShieldCheckBoxState()

        // Then
        assertEquals(expectedResult, result)
    }
}
