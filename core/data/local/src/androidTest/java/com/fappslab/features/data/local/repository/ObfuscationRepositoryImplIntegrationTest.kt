package com.fappslab.features.data.local.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.common.domain.usecase.DecodeParams
import com.fappslab.features.common.domain.usecase.EncodeParams
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.COLORED
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.InjectParams
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.PALETTE
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.SEED
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.createDataSource
import com.fappslab.features.data.local.repository.SeedcakeRepositoryImplFixtures.toColorPairs
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType
import com.fappslab.seedcake.libraries.extension.blankString
import com.fappslab.seedcake.libraries.extension.emptyString
import com.fappslab.seedcake.libraries.extension.splitToList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import stub.decryptParamsStub
import stub.encryptParamsStub
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
    fun encrypt_Should_ReturnEncryptedSeedPhrase_When_GivenEncryptParams() = runTest {
        // Given
        val encryptParams = encryptParamsStub
        val expectedResult = SEED

        // When
        val result = subject.encrypt(encryptParams)

        // Then
        val decryptParams = decryptParamsStub.copy(unreadableSeedPhrase = result)
        val decrypted = subject.decrypt(decryptParams)
        assertEquals(expectedResult, decrypted)
    }

    @Test
    fun decrypt_Should_ReturnDecryptedSeedPhrase_When_GivenDecryptParams() = runTest {
        // Given
        val expectedResult = SEED

        // When
        val result = subject.decrypt(decryptParamsStub)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun encodeSeedColor_Should_ReturnColoredSeedPalette_When_GivenSeed() = runTest {
        // Given
        val expectedResult = PALETTE.toColorPairs()
        val params = EncodeParams(SEED.split(blankString()))

        // When
        val result = subject.encodeColor(params)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun decodeSeedColor_Should_ReturnSeedPhrase_When_GivenColoredSeed() = runTest {
        // Given
        val expectedResult = SEED
        val params = DecodeParams(COLORED.split(blankString()))

        // When
        val result = subject.decodeColor(params)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun encrypt_Should_ThrowValidationException_When_ReadableSeedPhraseIsEmpty() = runTest {
        // Given
        val readableSeedPhrase = emptyList<String>()
        val encryptParams = encryptParamsStub.copy(readableSeedPhrase = readableSeedPhrase)
        val expectedResult = ValidationType.READABLE_SEED_PHRASE_EMPTY

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.encrypt(encryptParams)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun encrypt_Should_ThrowValidationException_When_ReadableSeedPhraseInvalidFormat() = runTest {
        // Given
        val readableSeedPhrase =
            "abandon banana captain dance eagle fabric galaxy habit ice jacket kangaroo language"
                .split(blankString())
        val encryptParams = encryptParamsStub.copy(readableSeedPhrase = readableSeedPhrase)
        val expectedResult = ValidationType.READABLE_SEED_PHRASE_INVALID_FORMAT

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.encrypt(encryptParams)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun encrypt_Should_ThrowValidationException_When_ReadableSeedPhraseHasInvalidSize() = runTest {
        // Given
        val readableSeedPhrase = listOf("gesture", "grow")
        val encryptParams = encryptParamsStub.copy(readableSeedPhrase = readableSeedPhrase)
        val expectedFirstResult = ValidationType.READABLE_SEED_PHRASE_INVALID_LENGTH
        val expectedFinalResult = "12, 24"

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.encrypt(encryptParams)
        }

        // Then
        assertEquals(expectedFirstResult, result.type)
        assertEquals(expectedFinalResult, result.message)
    }

    @Test
    fun encrypt_Should_ThrowValidationException_When_WordNotInList() = runTest {
        // Given
        val invalidWord = "fake"
        val readableSeedPhrase = SEED.split(blankString()).dropLast(1).plus(invalidWord)
        val encryptParams = encryptParamsStub.copy(readableSeedPhrase = readableSeedPhrase)
        val expectedResult = ValidationType.READABLE_SEED_PHRASE_WORD_NOT_IN_LIST

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.encrypt(encryptParams)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun encrypt_Should_ThrowValidationException_When_PassphraseIsEmpty() = runTest {
        // Given
        val encryptParams = encryptParamsStub.copy(passphrase = emptyString())
        val expectedResult = ValidationType.PASSPHRASE_EMPTY

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.encrypt(encryptParams)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun encrypt_Should_ThrowValidationException_When_PassphraseIsInvalid() = runTest {
        // Given
        val passphrase = "invalidPassphrase"
        val encryptParams = encryptParamsStub.copy(passphrase = passphrase)
        val expectedResult = ValidationType.PASSPHRASE_INVALID

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.encrypt(encryptParams)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun decrypt_Should_ThrowValidationException_When_UnreadableSeedPhraseIsEmpty() = runTest {
        // Given
        val unreadableSeedPhrase = emptyString()
        val decryptParams = decryptParamsStub.copy(unreadableSeedPhrase = unreadableSeedPhrase)
        val expectedResult = ValidationType.UNREADABLE_SEED_PHRASE_EMPTY

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.decrypt(decryptParams)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun decrypt_Should_ThrowValidationException_When_PassphraseIsEmpty() = runTest {
        // Given
        val passphrase = emptyString()
        val decryptParams = decryptParamsStub.copy(passphrase = passphrase)
        val expectedResult = ValidationType.PASSPHRASE_EMPTY

        // When / Then
        val result = assertFailsWith<ThrowableValidation> {
            subject.decrypt(decryptParams)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun decrypt_Should_ThrowValidationException_When_PassphraseIsInvalid() = runTest {
        // Given
        val passphrase = "invalidPassphrase"
        val decryptParams = decryptParamsStub.copy(passphrase = passphrase)
        val expectedResult = ValidationType.PASSPHRASE_INVALID

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.decrypt(decryptParams)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun encodeSeedColor_Should_ThrowThrowableValidation_When_ReadableSeedPhraseIsEmpty() = runTest {
        // Given
        val readableSeedPhrase = emptyString()
        val expectedResult = ValidationType.READABLE_SEED_PHRASE_EMPTY
        val params = EncodeParams(readableSeedPhrase.splitToList(blankString()))

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.encodeColor(params)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun encodeSeedColor_Should_ThrowValidationException_When_InvalidSeedPhrase() = runTest {
        // Given
        val readableSeedPhrase = "apple orange"
        val expectedFirstResult = ValidationType.READABLE_SEED_PHRASE_INVALID_LENGTH
        val expectedFinalResult = "12, 24"
        val params = EncodeParams(readableSeedPhrase.split(blankString()))

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.encodeColor(params)
        }

        // Then
        assertEquals(expectedFirstResult, result.type)
        assertEquals(expectedFinalResult, result.message)
    }

    @Test
    fun encodeSeedColor_Should_ThrowValidationException_When_WordNotInList() = runTest {
        // Given
        val invalidWord = "fake"
        val readableSeedPhrase = SEED.split(blankString())
            .dropLast(1).plus(invalidWord).joinToString(blankString())
        val expectedResult = ValidationType.READABLE_SEED_PHRASE_WORD_NOT_IN_LIST
        val params = EncodeParams(readableSeedPhrase.split(blankString()))

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.encodeColor(params)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun decodeSeedColor_Should_ThrowValidationException_When_BlankColoredSeedPhrase() = runTest {
        // Given
        val colorfulSeedPhrase = emptyString()
        val expectedExceptionType = ValidationType.BLANK_COLORED_SEED_EXCEPTION
        val params = DecodeParams(colorfulSeedPhrase.splitToList(blankString()))

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.decodeColor(params)
        }

        // Then
        assertEquals(expectedExceptionType, result.type)
    }

    @Test
    fun decodeSeedColor_Should_ThrowValidationException_When_NonSequentialColors() = runTest {
        // Given
        val colorfulSeedPhrase = "#FFFFFF #000000 #123456 #654321 #ABCDEF #FEDCBA #987654 #321098"
        val expectedExceptionType = ValidationType.SEQUENTIAL_COLOR_EXCEPTION
        val params = DecodeParams(colorfulSeedPhrase.split(blankString()))

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.decodeColor(params)
        }

        // Then
        assertEquals(expectedExceptionType, result.type)
    }

    @Test
    fun decodeSeedColor_Should_ThrowValidationException_When_InvalidHexFormat() = runTest {
        // Given
        val colorfulSeedPhrase = "#ZZZZZZ #123456 #654321 #ABCDEF #FEDCBA #987654 #321098 #000000"
        val expectedResult = ValidationType.INVALID_HEX_COLOR_EXCEPTION
        val params = DecodeParams(colorfulSeedPhrase.split(blankString()))

        // When
        val result = assertFailsWith<ThrowableValidation> {
            subject.decodeColor(params)
        }

        // Then
        assertEquals(expectedResult, result.type)
    }

    @Test
    fun decodeSeedColor_Should_ThrowValidationException_When_InvalidNumberOfColors() =
        runTest {
            // Given
            val colorfulSeedPhrase = "#123456 #654321 #ABCDEF"
            val expectedResult = ValidationType.INVALID_COLOR_FORMAT_EXCEPTION
            val params = DecodeParams(colorfulSeedPhrase.split(blankString()))

            // When
            val result = assertFailsWith<ThrowableValidation> {
                subject.decodeColor(params)
            }

            // Then
            assertEquals(expectedResult, result.type)
        }

    @Test
    fun decodeSeedColor_Should_ThrowValidationException_When_IndexIsOutOfBounds() =
        runTest {
            // Given
            val colorfulSeedPhrase =
                "#D5F31D #98EA1A #5BE11A #1ED81A #0232B6 #3F2CB6 #7C35B6 #B93EB6"
            val expectedResult = ValidationType.INDEX_OUT_OF_BOUNDS_COLOR_EXCEPTION
            val params = DecodeParams(colorfulSeedPhrase.split(blankString()))

            // When
            val result = assertFailsWith<ThrowableValidation> {
                subject.decodeColor(params)
            }

            // Then
            assertEquals(expectedResult, result.type)
        }
}
