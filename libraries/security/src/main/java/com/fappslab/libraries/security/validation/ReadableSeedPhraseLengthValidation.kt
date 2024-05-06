package com.fappslab.libraries.security.validation

import com.fappslab.libraries.security.validation.strategy.ValidationStrategy
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType
import com.fappslab.seedcake.libraries.extension.VALID_ENTROPY_LENGTHS

internal class ReadableSeedPhraseLengthValidation(
    private val readableSeedPhrase: List<String>
) : ValidationStrategy {
    override fun validate() {
        if (readableSeedPhrase.size !in VALID_ENTROPY_LENGTHS) throw ThrowableValidation(
            type = ValidationType.READABLE_SEED_PHRASE_INVALID_LENGTH,
            message = VALID_ENTROPY_LENGTHS.joinToString(separator = ", ")
        )
    }
}
