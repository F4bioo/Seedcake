package com.fappslab.libraries.security.validation

import com.fappslab.libraries.security.validation.strategy.ValidationStrategy
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType
import com.fappslab.seedcake.libraries.extension.isValidPassphrase

internal class PassphraseFormatValidation(
    private val passphrase: String
) : ValidationStrategy {
    override fun validate() {
        if (!passphrase.isValidPassphrase()) throw ThrowableValidation(
            type = ValidationType.PASSPHRASE_INVALID
        )
    }
}
