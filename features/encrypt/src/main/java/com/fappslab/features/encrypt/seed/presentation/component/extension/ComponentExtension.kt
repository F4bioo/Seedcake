package com.fappslab.features.encrypt.seed.presentation.component.extension

internal fun List<String>.nextPossibleChars(currentSequence: String): Set<Char> {
    return asSequence()
        .filter { it.startsWith(currentSequence) && currentSequence.length < it.length }
        .map { it[currentSequence.length] }
        .toSet()
}
