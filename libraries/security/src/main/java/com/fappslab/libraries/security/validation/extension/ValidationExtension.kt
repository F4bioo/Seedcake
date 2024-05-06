package com.fappslab.libraries.security.validation.extension

import com.fappslab.libraries.security.model.ThrowableValidation
import com.fappslab.libraries.security.model.ValidationType
import kotlinx.coroutines.TimeoutCancellationException
import javax.crypto.AEADBadTagException

private fun Throwable.toThrowable(defType: ValidationType): Throwable {
    return when (this) {
        is TimeoutCancellationException -> ThrowableValidation(ValidationType.DECRYPTION_TIMEOUT)
        is AEADBadTagException -> ThrowableValidation(ValidationType.VERIFICATION_FAILED)
        is IndexOutOfBoundsException -> ThrowableValidation(ValidationType.INDEX_OUT_OF_BOUNDS_COLOR_EXCEPTION)
        else -> ThrowableValidation(defType, message)
    }
}

internal fun <T> Result<T>.orParseError(defType: ValidationType): T =
    getOrElse { cause ->
        throw cause.toThrowable(defType)
    }

