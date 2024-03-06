package com.nestor.database.util

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun dateToString(input: Date): Long {
        return input.time
    }

    @TypeConverter
    fun stringToDate(millis: Long): Date {
        return Date(millis)
    }
}