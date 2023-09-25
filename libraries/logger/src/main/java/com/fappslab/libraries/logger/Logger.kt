package com.fappslab.libraries.logger

import com.fappslab.seedcake.libraries.extension.SECURE_PASSPHRASE_REGEX
import com.fappslab.seedcake.libraries.logger.BuildConfig

private const val PIN_PATTERN = "\\b\\d{4}\\b"

class Logger private constructor(private val logEngine: LogEngine) {

    fun d(message: String?) {
        if (BuildConfig.DEBUG) {
            logEngine.debug(message)
        }
    }

    fun i(message: String?) {
        logEngine.info(sanitize(message))
    }

    fun w(message: String?) {
        logEngine.warning(sanitize(message))
    }

    fun e(message: String?) {
        logEngine.error(sanitize(message))
    }


    private fun sanitize(message: String?): String {
        var sanitizedMessage = message?.replace(
            PIN_PATTERN.toRegex(),
            replacement = "[PIN]"
        )
        sanitizedMessage = sanitizedMessage?.replace(
            SECURE_PASSPHRASE_REGEX.toRegex(),
            replacement = "[PASSPHRASE]"
        )

        return sanitizedMessage.orEmpty()
    }

    companion object {
        val log: Logger = Logger(TimberLogEngine())
    }
}
