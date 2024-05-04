package com.fappslab.libraries.security.cryptography.model

import com.fappslab.features.common.domain.model.TransformationType
import com.fappslab.libraries.security.cryptography.MetadataPreset
import com.google.gson.annotations.SerializedName

internal data class PresetC20(
    @SerializedName("cs")
    override val cipherSpec: TransformationType = TransformationType.C20,
    @SerializedName("kd")
    override val keyDerivation: DerivationType = DerivationType.PHS512,
    @SerializedName("ka")
    override val keyAlgorithm: String = "ChaCha20",
    @SerializedName("ki")
    override val keyIterations: Int = 200_000,
    @SerializedName("kb")
    override val keyBits: Int = 256,
    @SerializedName("is")
    override val ivSize: Int = 12,
    @SerializedName("ss")
    override val saltSize: Int = 16
) : MetadataPreset()
