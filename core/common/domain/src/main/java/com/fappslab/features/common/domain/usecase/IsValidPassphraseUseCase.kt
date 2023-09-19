package com.fappslab.features.common.domain.usecase

private const val SECURE_PASSPHRASE_REGEX =
    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"

class IsValidPassphraseUseCase {

    operator fun invoke(passphrase: String): Boolean =
        SECURE_PASSPHRASE_REGEX.toRegex().matches(passphrase)
}
