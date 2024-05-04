package com.fappslab.libraries.security.cryptography

import com.fappslab.libraries.security.cryptography.model.PresetC20
import com.fappslab.libraries.security.cryptography.model.PresetCBC
import com.fappslab.libraries.security.cryptography.model.PresetCTR
import com.fappslab.libraries.security.cryptography.model.PresetGCM
import com.fappslab.seedcake.libraries.arch.testing.FileLoader
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class MetadataPresetTest {

    @Test
    fun `fromJson Should deserialize to PresetGCM When json contains cs equals to GCM`() {
        // Given
        val expectedJson = FileLoader.readFromJsonFile(jsonFile = "preset_gcm.json")
        val currentJson = MetadataPreset.toJson(PresetGCM())

        // When
        val result = MetadataPreset.fromJson(currentJson)

        // Then
        assertIs<PresetGCM>(result)
        assertEquals(expectedJson, currentJson)
    }

    @Test
    fun `fromJson Should deserialize to PresetC20 When json contains cs equals to C20`() {
        // Given
        val expectedJson = FileLoader.readFromJsonFile(jsonFile = "preset_c20.json")
        val currentJson = MetadataPreset.toJson(PresetC20())

        // When
        val result = MetadataPreset.fromJson(currentJson)

        // Then
        assertIs<PresetC20>(result)
        assertEquals(expectedJson, currentJson)
    }

    @Test
    fun `fromJson Should deserialize to PresetCTR When json contains cs equals to CTR`() {
        // Given
        val expectedJson = FileLoader.readFromJsonFile(jsonFile = "preset_ctr.json")
        val currentJson = MetadataPreset.toJson(PresetCTR())

        // When
        val result = MetadataPreset.fromJson(currentJson)

        // Then
        assertIs<PresetCTR>(result)
        assertEquals(expectedJson, currentJson)
    }

    @Test
    fun `fromJson Should deserialize to PresetCBC When json contains cs equals to CBC`() {
        // Given
        val expectedJson = FileLoader.readFromJsonFile(jsonFile = "preset_cbc.json")
        val currentJson = MetadataPreset.toJson(PresetCBC())

        // When
        val result = MetadataPreset.fromJson(currentJson)

        // Then
        assertIs<PresetCBC>(result)
        assertEquals(expectedJson, currentJson)
    }
}
