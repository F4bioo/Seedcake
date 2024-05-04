/*
 * Project: Seedcake
 * Repository: https://github.com/F4bioo/Seedcake
 * Author: @F4bioo
 *
 * Inspired by the work of @EnteroPositivo
 * Original Repository: https://github.com/enteropositivo/BIP39Colors
 * License: CC By https://github.com/EnteroPositivo/bip39Colors/blob/main/LICENSE.md
 */

package com.fappslab.libraries.security.bip39colors

import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType
import com.fappslab.seedcake.libraries.extension.blankString
import com.fappslab.seedcake.libraries.extension.emptyString
import com.fappslab.seedcake.libraries.extension.splitToList

class BIP39ColorsImpl(private val wordList: List<String>) : BIP39Colors {

    private val validator by lazy { BIP39ColorsValidator(wordList) }

    override suspend fun encodeSeedColor(readableSeedPhrase: String): List<Pair<String, String>> {
        val list = readableSeedPhrase.splitToList(blankString())
        validator.encodeValidation(list)

        val colors = mapSeedToColors(list)
        val rgbArr = colors.map { hexToRgb(it) }
        val colorHsv = rgbArr.map { mapOf("rgb" to it, "hsv" to rgbToHsv(it)) }

        val sortedColors = colorHsv
            .sortedWith(compareBy({ it["hsv"]?.get(0) }, { it["hsv"]?.get(2) }))
            .mapNotNull { it["rgb"]?.map { color -> color.toInt() } }

        return sortedColors.map { rgb ->
            val hexColor = rgbToHex(rgb)
            val textColor = if (calculateLuminance(rgb) > 0.5) {
                "#000000"
            } else "#FFFFFF"

            hexColor to textColor
        }
    }

    override suspend fun decodeSeedColor(colorfulSeedPhrase: String): String {
        val list = colorfulSeedPhrase.splitToList(blankString())
        validator.decodeValidation(list)

        val colorsInDecimal = list.map {
            it.substring(startIndex = 1).toInt(radix = 16).toString()
                .padStart(length = 8, padChar = '0')
        }.sorted()

        if (!isSequentialArray(colorsInDecimal)) throw ThrowableValidation(
            type = ValidationType.SEQUENTIAL_COLOR_EXCEPTION
        )

        return convertDecimalColorsToWords(colorsInDecimal)
    }

    private fun mapSeedToColors(readableSeedPhrase: List<String>): List<String> {
        val wordPositions = when {
            readableSeedPhrase.all { it.toIntOrNull() in 1..2048 } ->
                readableSeedPhrase.map { it.toInt() }.toMutableList()

            readableSeedPhrase.all { it in wordList } ->
                readableSeedPhrase.map { wordList.indexOf(it).inc() }.toMutableList()

            else -> throw ThrowableValidation(
                type = ValidationType.READABLE_SEED_PHRASE_WORD_NOT_IN_LIST
            )
        }

        return seedToColors(wordPositions)
    }

    private fun seedToColors(wordPositions: List<Int>): List<String> {
        val positionsText = wordPositions.joinToString(emptyString()) {
            it.toString().padStart(length = 4, padChar = '0')
        }
        val positionPieces = positionsText.chunked(6)

        return positionPieces.mapIndexed { index, piece ->
            val positionIndex = if (wordPositions.size == 12) {
                (index % 8) * 2 + (index / 8)
            } else index

            val colorSeed = positionIndex.toString() + piece

            val color = colorSeed.toInt()
                .toString(radix = 16)
                .padStart(length = 6, padChar = '0')
                .uppercase()

            "#$color"
        }
    }

    private fun convertDecimalColorsToWords(decArray: List<String>): String {
        val text = decArray.joinToString(emptyString()) { it.takeLast(n = 6) }
        val bip39Positions = text.chunked(size = 4)
        return bip39Positions.map { it.toInt() }
            .joinToString(blankString()) { wordList[it.dec()] }
    }

    private fun isSequentialArray(array: List<String>): Boolean {
        val expectedDiff = if (array.size == 8) 2 else 1

        return array.drop(n = 1).mapIndexed { index, s ->
            s.substring(0, 2).toInt() - array[index].substring(0, 2).toInt()
        }.all { it == expectedDiff }
    }

    private fun hexToRgb(hex: String): List<Int> {
        return listOf(
            hex.substring(1, 3).toInt(radix = 16),
            hex.substring(3, 5).toInt(radix = 16),
            hex.substring(5, 7).toInt(radix = 16)
        )
    }

    private fun rgbToHex(rgb: List<Int>): String {
        val r = rgb[0].toString(radix = 16).padStart(length = 2, padChar = '0')
        val g = rgb[1].toString(radix = 16).padStart(length = 2, padChar = '0')
        val b = rgb[2].toString(radix = 16).padStart(length = 2, padChar = '0')
        return "#$r$g$b".uppercase()
    }

    private fun rgbToHsv(rgb: List<Int>): List<Double> {
        val r = rgb[0] / 255.0
        val g = rgb[1] / 255.0
        val b = rgb[2] / 255.0
        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min
        val h = when (max) {
            min -> 0.0
            r -> ((60 * ((g - b) / delta) + 360) % 360)
            g -> (60 * ((b - r) / delta) + 120)
            else -> (60 * ((r - g) / delta) + 240)
        }
        val s = if (max != 0.0) {
            delta / max
        } else 0.0

        return listOf(h, s, max)
    }

    private fun calculateLuminance(rgb: List<Int>): Double {
        val r = rgb[0] / 255.0
        val g = rgb[1] / 255.0
        val b = rgb[2] / 255.0
        return 0.299 * r + 0.587 * g + 0.114 * b
    }
}
