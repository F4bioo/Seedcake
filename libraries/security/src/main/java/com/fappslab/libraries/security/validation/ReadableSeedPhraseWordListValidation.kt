package com.fappslab.libraries.security.validation

import com.fappslab.libraries.security.validation.strategy.ValidationStrategy
import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType

internal class ReadableSeedPhraseWordListValidation(
    private val readableSeedPhrase: List<String>,
    private val wordList: List<String>
) : ValidationStrategy {
    override fun validate() {
        if (!readableSeedPhrase.all { it in wordList }) throw ThrowableValidation(
            type = ValidationType.READABLE_SEED_PHRASE_WORD_NOT_IN_LIST
        )
    }
}
