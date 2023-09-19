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

import com.fappslab.seedcake.libraries.arch.exceptions.BlankColoredSeedException
import com.fappslab.seedcake.libraries.arch.exceptions.BlankSeedException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidColorException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidColorFormatException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidEntropyLengthException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidMnemonicSeedException
import com.fappslab.seedcake.libraries.arch.exceptions.InvalidWordException

private const val MIN_ENTROPY_LENGTH = 12
private const val MAX_ENTROPY_LENGTH = 24

@Suppress("MagicNumber", "ThrowsCount")
class BIP39ColorsImpl(private val wordList: List<String>) : BIP39Colors {

    override suspend fun encodeSeedColor(readableSeed: String): List<Pair<String, String>> {
        if (readableSeed.isBlank()) throw BlankSeedException()

        val colors = mapSeedToColors(readableSeed)
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

    override suspend fun decodeSeedColor(coloredSeed: String): String {
        if (coloredSeed.isBlank()) throw BlankColoredSeedException()

        val inputColors = coloredSeed.trim().split(' ')

        if (inputColors.size !in listOf(8, 16)) {
            throw InvalidColorFormatException()
        }

        if (inputColors.any { isValidHexColor(it).not() }) {
            throw InvalidColorFormatException()
        }

        val colors = inputColors.toMutableList()
        val decArray = colors.map {
            it.substring(1)
                .toInt(16).toString()
                .padStart(8, '0')
        }.sorted()

        if (!isSequentialArray(decArray)) {
            throw InvalidColorException()
        }

        val text = decArray.joinToString(separator = "") { it.takeLast(6) }
        val bip39Positions = text.chunked(4)

        if (bip39Positions.any { it.toIntOrNull() !in 1..2048 }) {
            throw InvalidColorException()
        }

        val wordPositions = bip39Positions.map { it.toInt() }.toMutableList()
        return wordPositions.joinToString(separator = " ") { wordList[it - 1] }
    }

    private fun mapSeedToColors(decryptedSeed: String): List<String> {
        val seed = decryptedSeed.trim().split(' ').map { it.lowercase() }

        val validEntropyLengths = listOf(MIN_ENTROPY_LENGTH, MAX_ENTROPY_LENGTH)
        if (seed.size !in validEntropyLengths) throw InvalidEntropyLengthException()
        if (!seed.none { it !in wordList }) throw InvalidMnemonicSeedException()

        val wordPositions = when {
            seed.all { it.toIntOrNull() in 1..2048 } ->
                seed.map { it.toInt() }.toMutableList()

            seed.all { it in wordList } ->
                seed.map { wordList.indexOf(it) + 1 }.toMutableList()

            else -> throw InvalidWordException()
        }

        return seedToColors(wordPositions)
    }

    private fun seedToColors(wordPositions: List<Int>): List<String> {
        val positionsText = wordPositions.joinToString(separator = "") {
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

    private fun isValidHexColor(color: String): Boolean {
        val isHex = color.substring(startIndex = 1).all { c -> c.isDigit() || c.isLetter() }
        return color.length == 7 && color.startsWith(char = '#') && isHex
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
