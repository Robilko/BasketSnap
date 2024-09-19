package ru.robilko.base.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val ISO_8601_WITH_TIMEZONE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"
const val SERVER_DATE_PATTERN = "yyyy-MM-dd"
const val HUMAN_DATE_PATTERN = "dd.MM.yyyy"

fun String.toDate(pattern: String, timeZoneId: String = "UTC"): Date? {
    return try {
        SimpleDateFormat(pattern, Locale.getDefault())
            .apply {  timeZone = TimeZone.getTimeZone(timeZoneId) }
            .parse(this)
    } catch (e: Exception) {
        null
    }
}

fun Date.toStringDate(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault())
        .format(this).toString()
}