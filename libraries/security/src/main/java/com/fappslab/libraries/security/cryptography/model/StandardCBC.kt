package com.fappslab.libraries.security.cryptography.model

import com.fappslab.libraries.security.cryptography.model.AlgorithmType.CBC
import com.fappslab.libraries.security.cryptography.model.DerivationType.PHS256
import com.google.gson.annotations.SerializedName

internal data class StandardCBC(
    @SerializedName("cs")
    override val cipherSpec: AlgorithmType = CBC,
    @SerializedName("kd")
    override val keyDerivation: DerivationType = PHS256,
    @SerializedName("ka")
    override val keyAlgorithm: String = "AES",
    @SerializedName("ki")
    override val keyIterations: Int = 150_000,
    @SerializedName("kb")
    override val keyBits: Int = 256,
    @SerializedName("is")
    override val ivSize: Int = 16,
    @SerializedName("ss")
    override val saltSize: Int = 16
) : MetadataPreset()
