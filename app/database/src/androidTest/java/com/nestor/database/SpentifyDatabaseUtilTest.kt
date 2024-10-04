package com.nestor.database

import android.content.Context
import androidx.room.Room

class SpentifyDatabaseUtilTest {
    companion object {
        fun createDb(context: Context): SpentifyDatabase {
            return Room
                .inMemoryDatabaseBuilder(context, SpentifyDatabase::class.java)
                .build()
        }
    }
}