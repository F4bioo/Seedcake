package com.fappslab.libraries.security.cryptography

import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType
import com.fappslab.seedcake.libraries.extension.VALID_ENTROPY_LENGTHS
import com.fappslab.seedcake.libraries.extension.emptyString
import com.fappslab.seedcake.libraries.extension.isValidPassphrase
import kotlinx.coroutines.TimeoutCancellationException
import java.security.MessageDigest
import javax.crypto.AEADBadTagException

private const val SHA_256_ALGORITHM = "SHA-256"
private const val BINARY_RADIX = 2
private const val BITS_FOR_WORD = 11
private const val BYTE_SIZE = 8

internal class CryptoValidator(private val wordList: List<String>) {

    fun encryptValidation(readableSeedPhrase: List<String>, passphrase: String) {
        validateSeedPhrase(readableSeedPhrase)
        validatePassphrase(passphrase)
    }

    fun decryptValidation(unreadableSeedPhrase: String, passphrase: String) {
        if (unreadableSeedPhrase.isBlank()) throw ThrowableValidation(
            type = ValidationType.UNREADABLE_SEED_PHRASE_EMPTY
        )
        validatePassphrase(passphrase)
    }

    private fun validateSeedPhrase(readableSeedPhrase: List<String>) {
        if (readableSeedPhrase.isEmpty()) throw ThrowableValidation(
            type = ValidationType.READABLE_SEED_PHRASE_EMPTY
        )
        if (readableSeedPhrase.size !in VALID_ENTROPY_LENGTHS) throw ThrowableValidation(
            type = ValidationType.READABLE_SEED_PHRASE_INVALID_LENGTH,
            message = VALID_ENTROPY_LENGTHS.joinToString(separator = ", ")
        )
        if (!readableSeedPhrase.all { it in wordList }) throw ThrowableValidation(
            type = ValidationType.READABLE_SEED_PHRASE_WORD_NOT_IN_LIST
        )
        if (!isValidSeedPhrase(readableSeedPhrase)) throw ThrowableValidation(
            type = ValidationType.READABLE_SEED_PHRASE_INVALID_FORMAT
        )
    }

    private fun validatePassphrase(passphrase: String) {
        if (passphrase.isBlank()) throw ThrowableValidation(
            type = ValidationType.PASSPHRASE_EMPTY
        )
        if (!passphrase.isValidPassphrase()) throw ThrowableValidation(
            type = ValidationType.PASSPHRASE_INVALID
        )
    }

    private fun isValidSeedPhrase(readableSeedPhrase: List<String>): Boolean {
        val binaryString = readableSeedPhrase
            .joinToString(emptyString(), transform = ::wordToBinaryString)

        val entropyBits = binaryString.dropLast(n = readableSeedPhrase.size / 3)
        val checksumBits = binaryString.takeLast(n = readableSeedPhrase.size / 3)

        val entropyBytes = entropyBits.chunked(BYTE_SIZE)
            .map { it.toInt(BINARY_RADIX).toByte() }.toByteArray()
        val calculatedChecksumBits = MessageDigest.getInstance(SHA_256_ALGORITHM)
            .digest(entropyBytes)
            .joinToString(emptyString()) { byteToBinaryString(it) }
            .take(readableSeedPhrase.size / 3)

        return checksumBits == calculatedChecksumBits
    }

    private fun wordToBinaryString(word: String): String {
        return wordList.indexOf(word)
            .toString(BINARY_RADIX)
            .padStart(BITS_FOR_WORD, padChar = '0')
    }

    private fun byteToBinaryString(byte: Byte): String {
        return byte.toInt().and(0xFF)
            .toString(BINARY_RADIX)
            .padStart(BYTE_SIZE, padChar = '0')
    }
}

private fun Throwable.toThrowable(defType: ValidationType): Throwable {
    return when (this) {
        is TimeoutCancellationException -> ThrowableValidation(ValidationType.DECRYPTION_TIMEOUT)
        is AEADBadTagException -> ThrowableValidation(ValidationType.VERIFICATION_FAILED)
        else -> ThrowableValidation(defType, message)
    }
}

internal fun <T> Result<T>.orParseError(defType: ValidationType): T =
    getOrElse { cause ->
        throw cause.toThrowable(defType)
    }
