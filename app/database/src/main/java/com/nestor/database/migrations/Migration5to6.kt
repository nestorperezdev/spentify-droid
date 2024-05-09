package com.nestor.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration5to6: Migration(5, 6){
    /**
     * Add exchange_id column to currency table, but first delete every row. to force resync.
     */
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DELETE FROM currency")
        db.execSQL("ALTER TABLE currency ADD COLUMN exchange_id TEXT NOT NULL")
    }
}