package com.fappslab.features.encrypt.secret.domain.usecase

import com.fappslab.libraries.security.model.SecureString
import com.fappslab.seedcake.libraries.extension.emptyString
import java.security.SecureRandom

private const val NUMBERS = "0123456789"
private const val LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz"
private const val UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
private const val SPECIAL_CHARACTERS = "!@#\$%^&*()-_=+[]{}|;:'\",.<>?/~"

internal const val MIN_PASSPHRASE_LENGTH = 8
internal const val MAX_PASSPHRASE_LENGTH = 100

internal class PassphraseGeneratorUseCase {

    private val secureRandom = SecureRandom()
    private val allChars = "$NUMBERS$LOWER_CASE_LETTERS$UPPER_CASE_LETTERS$SPECIAL_CHARACTERS"

    operator fun invoke(length: Int): SecureString {
        require(length in MIN_PASSPHRASE_LENGTH..MAX_PASSPHRASE_LENGTH) {
            "Password length must be between $MIN_PASSPHRASE_LENGTH and $MAX_PASSPHRASE_LENGTH characters. Got $length."
        }

        val passwordChars = mutableListOf<Char>().apply {
            add(NUMBERS.randomChar())
            add(LOWER_CASE_LETTERS.randomChar())
            add(UPPER_CASE_LETTERS.randomChar())
            add(SPECIAL_CHARACTERS.randomChar())
        }

        while (passwordChars.size < length) {
            passwordChars.add(allChars.randomChar())
        }

        return SecureString.from(
            passwordChars.shuffled(secureRandom)
                .joinToString(emptyString())
        )
    }

    private fun String.randomChar(): Char {
        return this[secureRandom.nextInt(this.length)]
    }
}
