package com.fappslab.seedcake.libraries.extension

fun String?.orDash() = this ?: "---"

fun String.capitalize(): String =
    replaceFirstChar { it.uppercase() }
