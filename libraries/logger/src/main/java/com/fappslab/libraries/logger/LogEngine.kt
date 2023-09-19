package com.fappslab.libraries.logger

interface LogEngine {
    fun debug(message: String?)
    fun info(message: String?)
    fun warning(message: String?)
    fun error(message: String?)
}
