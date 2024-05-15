package com.nestor.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration6to7 : Migration(6, 7) {
    /**
     * Create table expense with fields defined in ExpenseEntity
     */
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE expense (id TEXT PRIMARY KEY, user_uuid TEXT NOT NULL, description TEXT NOT NULL, date INTEGER NOT NULL, stored_at INTEGER NOT NULL, amount REAL NOT NULL, cursor INTEGER NOT NULL, usd_value REAL NOT NULL)")
    }
}