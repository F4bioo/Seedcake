package com.fappslab.libraries.security.validation.factory

import com.fappslab.features.common.domain.model.ParamsFactory
import com.fappslab.features.common.domain.usecase.DecodeParams
import com.fappslab.features.common.domain.usecase.DecryptParams
import com.fappslab.features.common.domain.usecase.EncodeParams
import com.fappslab.features.common.domain.usecase.EncryptParams
import com.fappslab.libraries.security.validation.ColorfulSeedPhraseEmptyValidation
import com.fappslab.libraries.security.validation.ColorfulSeedPhraseFormatValidation
import com.fappslab.libraries.security.validation.HexColorFormatValidation
import com.fappslab.libraries.security.validation.PassphraseEmptyValidation
import com.fappslab.libraries.security.validation.PassphraseFormatValidation
import com.fappslab.libraries.security.validation.ReadableSeedPhraseEmptyValidation
import com.fappslab.libraries.security.validation.ReadableSeedPhraseFormatValidation
import com.fappslab.libraries.security.validation.ReadableSeedPhraseLengthValidation
import com.fappslab.libraries.security.validation.ReadableSeedPhraseWordListValidation
import com.fappslab.libraries.security.validation.UnreadableSeedPhraseEmptyValidation
import com.fappslab.libraries.security.validation.strategy.StrategyType
import com.fappslab.libraries.security.validation.strategy.ValidationStrategy

internal class ValidationFactory(
    private val wordList: List<String>
) {

    fun getValidations(type: StrategyType, params: ParamsFactory): List<ValidationStrategy> {
        return when (type) {
            StrategyType.ENCODE -> {
                val (readableSeedPhrase) = params as EncodeParams
                listOf(
                    ReadableSeedPhraseEmptyValidation(readableSeedPhrase),
                    ReadableSeedPhraseLengthValidation(readableSeedPhrase),
                    ReadableSeedPhraseWordListValidation(readableSeedPhrase, wordList)
                )
            }

            StrategyType.DECODE -> {
                val (colorfulSeedPhrase) = params as DecodeParams
                listOf(
                    ColorfulSeedPhraseEmptyValidation(colorfulSeedPhrase),
                    ColorfulSeedPhraseFormatValidation(colorfulSeedPhrase),
                    HexColorFormatValidation(colorfulSeedPhrase)
                )
            }

            StrategyType.ENCRYPT -> {
                val (_, readableSeedPhrase, passphrase) = params as EncryptParams
                listOf(
                    ReadableSeedPhraseEmptyValidation(readableSeedPhrase),
                    ReadableSeedPhraseLengthValidation(readableSeedPhrase),
                    ReadableSeedPhraseWordListValidation(readableSeedPhrase, wordList),
                    ReadableSeedPhraseFormatValidation(readableSeedPhrase, wordList),
                    PassphraseEmptyValidation(passphrase),
                    PassphraseFormatValidation(passphrase),
                )
            }

            StrategyType.DECRYPT -> {
                val (unreadableSeedPhrase, passphrase) = params as DecryptParams
                listOf(
                    UnreadableSeedPhraseEmptyValidation(unreadableSeedPhrase),
                    PassphraseEmptyValidation(passphrase),
                    PassphraseFormatValidation(passphrase),
                )
            }
        }
    }
}
