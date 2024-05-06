package com.fappslab.libraries.security.validation

import com.fappslab.libraries.security.validation.strategy.ValidationStrategy
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType

internal class ReadableSeedPhraseEmptyValidation(
    private val readableSeedPhrase: List<String>
) : ValidationStrategy {
    override fun validate() {
        if (readableSeedPhrase.isEmpty()) throw ThrowableValidation(
            type = ValidationType.READABLE_SEED_PHRASE_EMPTY
        )
    }
}
