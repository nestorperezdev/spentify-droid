package com.nestor.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nestor.database.data.dashboard.DashboardDao
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.database.util.DateConverter

@Database(entities = [DashboardEntity::class], version = 3)
@TypeConverters(DateConverter::class)
abstract class SpentifyDatabase : RoomDatabase() {
    abstract fun dashboardDao(): DashboardDao
}