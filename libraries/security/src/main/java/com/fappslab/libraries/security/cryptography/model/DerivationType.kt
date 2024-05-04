package com.fappslab.libraries.security.cryptography.model

internal enum class DerivationType(val type: String) {
    PHS512(type = "PBKDF2WithHmacSHA512"),
    PHS256(type = "PBKDF2WithHmacSHA256");
}
