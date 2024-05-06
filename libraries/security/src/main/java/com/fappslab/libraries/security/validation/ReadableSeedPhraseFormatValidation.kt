package com.fappslab.libraries.security.validation

import com.fappslab.libraries.security.validation.strategy.ValidationStrategy
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType
import com.fappslab.seedcake.libraries.extension.emptyString
import java.security.MessageDigest

private const val SHA_256_ALGORITHM = "SHA-256"
private const val BINARY_RADIX = 2
private const val BITS_FOR_WORD = 11
private const val BYTE_SIZE = 8

internal class ReadableSeedPhraseFormatValidation(
    private val readableSeedPhrase: List<String>,
    private val wordList: List<String>
) : ValidationStrategy {

    override fun validate() {
        if (!isValidSeedPhrase(readableSeedPhrase)) throw ThrowableValidation(
            type = ValidationType.READABLE_SEED_PHRASE_INVALID_FORMAT
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
