package com.fappslab.libraries.security.bip39colors

import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType
import com.fappslab.seedcake.libraries.extension.VALID_ENTROPY_LENGTHS

internal class BIP39ColorsValidator(private val wordList: List<String>) {

    fun encodeValidation(readableSeedPhrase: List<String>) {
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
    }

    fun decodeValidation(colorfulSeedPhrase: List<String>) {
        if (colorfulSeedPhrase.isEmpty()) throw ThrowableValidation(
            type = ValidationType.BLANK_COLORED_SEED_EXCEPTION
        )
        if (colorfulSeedPhrase.size !in listOf(8, 16)) throw ThrowableValidation(
            type = ValidationType.INVALID_COLOR_FORMAT_EXCEPTION
        )
        if (colorfulSeedPhrase.any { !isValidHexColor(color = it) }) throw ThrowableValidation(
            type = ValidationType.INVALID_HEX_COLOR_EXCEPTION
        )
    }

    private fun isValidHexColor(color: String): Boolean {
        return color.matches("^#[0-9A-Fa-f]{6}$".toRegex())
    }
}
