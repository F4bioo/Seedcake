package com.fappslab.libraries.security.validation.strategy

internal interface ValidationStrategy {
    fun validate()
}

internal enum class StrategyType {
    ENCODE, DECODE, ENCRYPT, DECRYPT
}
