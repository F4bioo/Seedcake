package com.fappslab.features.data.local.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.COLORED
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.ENCRYPTED
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.InjectParams
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.PALETTE
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.PASSPHRASE
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.SEED
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.createDataSource
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.toColorPairs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class ObfuscationRepositoryImplIntegrationTest {

    private val context by lazy {
        ApplicationProvider.getApplicationContext<Context>()
    }

    private lateinit var subject: ObfuscationRepository

    @Before
    fun setUp() {
        subject = ObfuscationRepositoryImpl(
            dataSource = createDataSource(
                params = InjectParams(context)
            )
        )
    }

    @Test
    fun getWordList_Should_ReturnListOfWords_When_Invoked() = runTest {
        // Given
        val expectedResult = 2048

        // When
        val result = subject.getWordList()

        // Then
        assertEquals(expectedResult, result.size)
    }

    @Test
    fun encrypt_Should_ReturnEncryptedSeed_When_GivenSeedAndPassphrase() = runTest {
        // Given
        val expectedResult = SEED

        // When
        val result = subject.encrypt(SEED.split(" "), PASSPHRASE)

        // Then
        val decrypted = subject.decrypt(result, PASSPHRASE)
        assertEquals(expectedResult, decrypted)
    }

    @Test
    fun decrypt_Should_ReturnDecryptedSeed_When_GivenEncryptedSeedAndPassphrase() = runTest {
        // Given
        val expectedResult = SEED

        // When
        val result = subject.decrypt(ENCRYPTED, PASSPHRASE)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun encodeSeedColor_Should_ReturnColoredSeedPalette_When_GivenSeed() = runTest {
        // Given
        val expectedResult = PALETTE.toColorPairs()

        // When
        val result = subject.encodeSeedColor(SEED)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun decodeSeedColor_Should_ReturnSeed_When_GivenColoredSeed() = runTest {
        // Given
        val expectedResult = SEED

        // When
        val result = subject.decodeSeedColor(COLORED)

        // Then
        assertEquals(expectedResult, result)
    }
}
