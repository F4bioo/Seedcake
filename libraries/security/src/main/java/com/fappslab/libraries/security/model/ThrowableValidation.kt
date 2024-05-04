package com.fappslab.libraries.security.model

data class ThrowableValidation(
    val type: ValidationType,
    override val message: String? = null
) : Throwable(message)
