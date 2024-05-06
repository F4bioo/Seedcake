package com.fappslab.libraries.security.validation

import com.fappslab.libraries.security.validation.strategy.ValidationStrategy
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType

internal class ColorfulSeedPhraseFormatValidation(
    private val colorfulSeedPhrase: List<String>
) : ValidationStrategy {
    override fun validate() {
        if (colorfulSeedPhrase.size !in listOf(8, 16)) throw ThrowableValidation(
            type = ValidationType.INVALID_COLOR_FORMAT_EXCEPTION
        )
    }
}
