package com.fappslab.seedcake.libraries.arch.dateformat

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.ISODateTimeFormat
import java.util.Locale

const val DATE_TIME_PATTERN = "dd-MMM-yyyy, hh:mm a"
const val DATE_PATTERN = "dd/MM/yyyy"
private const val DASH = "---"

fun Long.toDateFormatted(pattern: String = DATE_TIME_PATTERN): String {
    return runCatching {
        val dateTime = DateTime(this)
        val formatted = DateTimeFormat.forPattern(pattern)
        formatted.withLocale(Locale.ENGLISH).print(dateTime)
    }.getOrElse { DASH }
}

fun String.toDateFormatted(pattern: String = DATE_TIME_PATTERN): String {
    return runCatching {
        val dateTime = DateTime(this)
        val formatted = DateTimeFormat.forPattern(pattern)
        formatted.withLocale(Locale.ENGLISH).print(dateTime)
    }.getOrElse { DASH }
}

fun DateTime.toRawDateTime(): String {
    return toString(ISODateTimeFormat.dateTime())
}

fun LocalDate.toRawLocalDate(): String {
    return toString(ISODateTimeFormat.dateTime())
}
