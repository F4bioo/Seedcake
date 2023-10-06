package com.fappslab.libraries.security.cryptography.model

import com.google.gson.Gson
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

private const val GCM_TAG_LENGTH_BITS = 128

abstract class MetadataPreset {
    abstract val cipherSpec: AlgorithmType
    abstract val keyDerivation: DerivationType
    abstract val keyAlgorithm: String
    abstract val keyIterations: Int
    abstract val keyBits: Int
    abstract val ivSize: Int
    abstract val saltSize: Int

    fun createCipherSpec(iv: ByteArray, type: AlgorithmType): AlgorithmParameterSpec {
        return when (type) {
            AlgorithmType.GCM -> GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
            AlgorithmType.CBC -> IvParameterSpec(iv)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }

    fun fromJson(json: String, classOfT: Class<out MetadataPreset>): MetadataPreset {
        return Gson().fromJson(json, classOfT)
    }
}
