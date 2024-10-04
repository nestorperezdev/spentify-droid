package com.nestor.common.util

import java.util.Calendar
import java.util.Date

/**
 * Will return the month start and end date of the given date.
 */
fun getStartAndEndOfDate(input: Date): Pair<Date, Date> {
    val calendar = Calendar.getInstance()
    calendar.time = input
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val start = calendar.time
    calendar.add(Calendar.MONTH, 1)
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val end = calendar.time
    return Pair(start, end)
}