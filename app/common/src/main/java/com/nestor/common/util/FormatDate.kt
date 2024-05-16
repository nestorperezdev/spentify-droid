package com.nestor.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//Get app locale

/**
 * Will become Monday, 1st January
 */
fun Date.formatWithDayAndDate(): String {
    return SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(this)
}