package com.fappslab.libraries.security.cryptography

import com.fappslab.features.common.domain.model.TransformationType
import com.fappslab.libraries.security.cryptography.model.DerivationType
import com.fappslab.libraries.security.cryptography.model.extension.toMetadata
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

internal abstract class MetadataPreset {
    abstract val cipherSpec: TransformationType
    abstract val keyDerivation: DerivationType
    abstract val keyAlgorithm: String
    abstract val keyIterations: Int
    abstract val keyBits: Int
    abstract val ivSize: Int
    abstract val saltSize: Int

    companion object {
        fun toJson(metadata: MetadataPreset): String {
            return Gson().toJson(metadata)
        }

        fun fromJson(metadataJson: String): MetadataPreset {
            val clasOfT = MetadataPreset::class.java
            val gson = GsonBuilder()
                .registerTypeAdapter(clasOfT, MetadataPresetDeserializer())
                .create()
            return gson.fromJson(metadataJson, clasOfT)
        }
    }
}

private class MetadataPresetDeserializer : JsonDeserializer<MetadataPreset> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): MetadataPreset {
        val csValue = json.asJsonObject.get("cs").asString
        val targetClass = TransformationType.valueOf(csValue).toMetadata()
        return context.deserialize(json, targetClass::class.java)
    }
}
