package com.fappslab.seedcake.libraries.extension

val VALID_ENTROPY_LENGTHS: List<Int> = listOf(12, 24)

fun List<String>.filterNotBlank() =
    filter { it.isNotBlank() }
