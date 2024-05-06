package com.fappslab.libraries.security.validation

import com.fappslab.libraries.security.validation.strategy.ValidationStrategy
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType

internal class PassphraseEmptyValidation(
    private val passphrase: String
) : ValidationStrategy {
    override fun validate() {
        if (passphrase.isBlank()) throw ThrowableValidation(
            type = ValidationType.PASSPHRASE_EMPTY
        )
    }
}
