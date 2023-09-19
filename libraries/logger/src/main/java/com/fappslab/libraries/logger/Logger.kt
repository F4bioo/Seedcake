package com.fappslab.libraries.logger

import com.fappslab.seedcake.libraries.logger.BuildConfig

private const val PIN_PATTERN = "\\b\\d{4}\\b"
private const val PASSPHRASE_PATTERN =
    "((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,})"

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
            PASSPHRASE_PATTERN.toRegex(),
            replacement = "[PASSPHRASE]"
        )

        return sanitizedMessage.orEmpty()
    }

    companion object {
        val log: Logger = Logger(TimberLogEngine())
    }
}
