package ru.robilko.base.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val ISO_8601_WITH_TIMEZONE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"
const val SERVER_DATE_PATTERN = "yyyy-MM-dd"
const val HUMAN_DATE_PATTERN = "dd.MM.yyyy"
const val HUMAN_DATE_DAY_OF_WEEK_TIME_PATTERN = "dd.MM.yyyy, EEE HH:mm"
const val HUMAN_DATE_DAY_OF_WEEK_TIME_PATTERN_2 = "dd.MM.yyyy,\nEEE HH:mm"

fun String.toDate(pattern: String, timeZoneId: String = "UTC"): Date? {
    return try {
        SimpleDateFormat(pattern, Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone(timeZoneId) }
            .parse(this)
    } catch (e: Exception) {
        null
    }
}

fun Date.toStringDate(pattern: String): String? {
    return try {
        SimpleDateFormat(pattern, Locale.getDefault())
            .format(this).toString()
    } catch (e: Exception) {
        null
    }
}

fun Date.isSameDay(anotherDate: Date): Boolean {
    val calendar1 = Calendar.getInstance().apply { time = this@isSameDay }
    val calendar2 = Calendar.getInstance().apply { time = anotherDate }

    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}

fun Date.isToday(): Boolean = this.isSameDay(Date())

fun Date.isTodayOrAfter(): Boolean {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    return this.after(today) || this == today
}
