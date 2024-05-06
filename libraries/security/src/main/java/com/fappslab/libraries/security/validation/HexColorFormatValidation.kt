package com.fappslab.libraries.security.validation

import com.fappslab.libraries.security.validation.strategy.ValidationStrategy
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType

internal class HexColorFormatValidation(
    private val colorfulSeedPhrase: List<String>
) : ValidationStrategy {
    override fun validate() {
        if (colorfulSeedPhrase.any { !isValidHexColor(color = it) }) throw ThrowableValidation(
            type = ValidationType.INVALID_HEX_COLOR_EXCEPTION
        )
    }

    private fun isValidHexColor(color: String): Boolean {
        return color.matches("^#[0-9A-Fa-f]{6}$".toRegex())
    }
}
