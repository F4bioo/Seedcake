package com.fappslab.features.common.domain.model

import java.security.spec.AlgorithmParameterSpec
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

private const val GCM_TAG_LENGTH_BITS = 128

enum class TransformationType(val type: String) {
    GCM(type = "AES/GCM/NoPadding") {
        override fun parameterSpec(iv: ByteArray): AlgorithmParameterSpec {
            return GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        }
    },
    C20(type = "ChaCha20-Poly1305") {
        override fun parameterSpec(iv: ByteArray): AlgorithmParameterSpec {
            return IvParameterSpec(iv)
        }
    },
    CTR(type = "AES/CTR/NoPadding") {
        override fun parameterSpec(iv: ByteArray): AlgorithmParameterSpec {
            return IvParameterSpec(iv)
        }
    },
    CBC(type = "AES/CBC/PKCS5Padding") {
        override fun parameterSpec(iv: ByteArray): AlgorithmParameterSpec {
            return IvParameterSpec(iv)
        }
    };

    abstract fun parameterSpec(iv: ByteArray): AlgorithmParameterSpec
}
