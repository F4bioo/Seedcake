package com.fappslab.libraries.security.validation

import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType
import com.fappslab.libraries.security.validation.strategy.ValidationStrategy

internal class UnreadableSeedPhraseEmptyValidation(
    private val unreadableSeedPhrase: String
) : ValidationStrategy {
    override fun validate() {
        if (unreadableSeedPhrase.isBlank()) throw ThrowableValidation(
            type = ValidationType.UNREADABLE_SEED_PHRASE_EMPTY
        )
    }
}
