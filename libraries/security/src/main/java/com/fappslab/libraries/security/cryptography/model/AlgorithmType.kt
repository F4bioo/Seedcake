package com.fappslab.libraries.security.cryptography.model

enum class AlgorithmType(val type: String) {
    GCM(type = "AES/GCM/NoPadding"),
    CBC(type = "AES/CBC/PKCS5Padding");
}
