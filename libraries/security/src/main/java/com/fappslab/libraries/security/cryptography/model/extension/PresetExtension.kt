package com.fappslab.libraries.security.cryptography.model.extension

import com.fappslab.features.common.domain.model.TransformationType
import com.fappslab.libraries.security.cryptography.MetadataPreset
import com.fappslab.libraries.security.cryptography.model.PresetCBC
import com.fappslab.libraries.security.cryptography.model.PresetCTR
import com.fappslab.libraries.security.cryptography.model.PresetGCM
import com.fappslab.libraries.security.cryptography.model.PresetC20

internal fun TransformationType.toMetadata(): MetadataPreset {
    return when (this) {
        TransformationType.GCM -> PresetGCM()
        TransformationType.C20 -> PresetC20()
        TransformationType.CTR -> PresetCTR()
        TransformationType.CBC -> PresetCBC()
    }
}
