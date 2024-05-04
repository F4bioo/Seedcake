package com.fappslab.libraries.security.cryptography.model

import com.fappslab.features.common.domain.model.TransformationType
import com.fappslab.libraries.security.cryptography.MetadataPreset
import com.google.gson.annotations.SerializedName

internal data class PresetCTR(
    @SerializedName("cs")
    override val cipherSpec: TransformationType = TransformationType.CTR,
    @SerializedName("kd")
    override val keyDerivation: DerivationType = DerivationType.PHS512,
    @SerializedName("ka")
    override val keyAlgorithm: String = "AES",
    @SerializedName("ki")
    override val keyIterations: Int = 200_000,
    @SerializedName("kb")
    override val keyBits: Int = 256,
    @SerializedName("is")
    override val ivSize: Int = 16,
    @SerializedName("ss")
    override val saltSize: Int = 16
) : MetadataPreset()
