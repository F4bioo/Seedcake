package com.fappslab.libraries.security.validation

import com.fappslab.libraries.security.validation.strategy.ValidationStrategy
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType

internal class ColorfulSeedPhraseEmptyValidation(
    private val colorfulSeedPhrase: List<String>
) : ValidationStrategy {
    override fun validate() {
        if (colorfulSeedPhrase.isEmpty()) throw ThrowableValidation(
            type = ValidationType.BLANK_COLORED_SEED_EXCEPTION
        )
    }
}
