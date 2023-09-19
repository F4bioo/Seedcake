package com.fappslab.features.about.presentation.extension

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.fappslab.seedcake.libraries.extension.orDash
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val DATE_PATTERN = "EEE, d MMM yyyy HH:mm"
private const val YEAR_OFFSET_DIVISOR = 100_000_000
private const val BASE_YEAR = 2020
private const val MONTH_DIVISOR = 1_000_000
private const val DAY_DIVISOR = 10_000
private const val HOUR_DIVISOR = 100

private fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int = 0): PackageInfo =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
    } else {
        @Suppress("DEPRECATION")
        getPackageInfo(packageName, flags)
    }

internal fun Context.getAppVersionName(): String? {
    return runCatching {
        val packageInfo = packageManager.getPackageInfoCompat(packageName, flags = 0)
        packageInfo.versionName.orDash()
    }.getOrNull()
}

internal fun Context.getAppVersionCode(): String? {
    return runCatching {
        val packageInfo = packageManager.getPackageInfoCompat(packageName, flags = 0)
        PackageInfoCompat.getLongVersionCode(packageInfo).toString()
    }.getOrNull()
}

internal fun Context.getReleaseDate(): String? {
    return runCatching {
        val packageInfo = packageManager.getPackageInfoCompat(packageName, flags = 0)
        val versionCode = PackageInfoCompat.getLongVersionCode(packageInfo).toInt()
        val localDateTime = versionCode.toLocalDateTime()

        localDateTime.format(DateTimeFormatter.ofPattern(DATE_PATTERN))
    }.getOrNull()
}

private fun Int.toLocalDateTime(): LocalDateTime {
    val yearOffset = this / YEAR_OFFSET_DIVISOR
    val year = BASE_YEAR + yearOffset
    var remaining = this % YEAR_OFFSET_DIVISOR

    val month = remaining / MONTH_DIVISOR
    remaining %= MONTH_DIVISOR

    val day = remaining / DAY_DIVISOR
    remaining %= DAY_DIVISOR

    val hour = remaining / HOUR_DIVISOR
    val minute = remaining % HOUR_DIVISOR

    return LocalDateTime.of(year, month, day, hour, minute)
}
