package com.nestor.common.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun parseISODate(isoDate: String): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    return try {
        format.parse(isoDate)
    } catch (exception: ParseException) {
        return null
    }
}