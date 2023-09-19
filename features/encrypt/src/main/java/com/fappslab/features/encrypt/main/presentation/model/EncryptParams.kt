package com.fappslab.features.encrypt.main.presentation.model

internal data class EncryptParams(
    val alias: String,
    val passphrase1: String,
    val passphrase2: String,
    val seed: List<String>
)
