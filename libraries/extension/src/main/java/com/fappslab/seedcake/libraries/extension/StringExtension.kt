package com.fappslab.seedcake.libraries.extension

const val SECURE_PASSPHRASE_REGEX =
    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?=\\S+$).{8,}$"

fun String?.orDash() = this ?: "---"

fun String.capitalize(): String =
    replaceFirstChar { it.uppercase() }

fun String.isValidPassphrase(): Boolean {
    return SECURE_PASSPHRASE_REGEX.toRegex().matches(this)
}
