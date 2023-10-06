package com.fappslab.libraries.security.cryptography.model

import com.fappslab.libraries.security.cryptography.model.AlgorithmType.GCM
import com.fappslab.libraries.security.cryptography.model.DerivationType.PHS512
import com.google.gson.annotations.SerializedName

internal data class StrongGCM(
    @SerializedName("cs")
    override val cipherSpec: AlgorithmType = GCM,
    @SerializedName("kd")
    override val keyDerivation: DerivationType = PHS512,
    @SerializedName("ka")
    override val keyAlgorithm: String = "AES",
    @SerializedName("ki")
    override val keyIterations: Int = 200_000,
    @SerializedName("kb")
    override val keyBits: Int = 256,
    @SerializedName("is")
    override val ivSize: Int = 12,
    @SerializedName("ss")
    override val saltSize: Int = 16
) : MetadataPreset()
