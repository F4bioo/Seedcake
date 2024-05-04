package com.fappslab.seedcake.libraries.arch.testing

import androidx.annotation.VisibleForTesting
import com.fappslab.seedcake.libraries.extension.emptyString
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.lang.reflect.Modifier

@Suppress("MaxLineLength")
@VisibleForTesting(otherwise = Modifier.PRIVATE)
object FileLoader {

    fun readFromJsonFile(jsonFile: String): String {
        val classLoader = Thread.currentThread().contextClassLoader
        val inputStream = classLoader?.getResourceAsStream(jsonFile)
            ?: throw FileNotFoundException("Resource '$jsonFile' not found. Ensure it's in 'src/main/resources' or 'src/test/resources'.")
        return inputStream.bufferedReader()
            .use(BufferedReader::readText)
            .replace("\\s".toRegex(), emptyString())
    }
}
