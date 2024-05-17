package com.nestor.database.migrations

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

@DeleteTable(tableName = "expense")
class AutoMigration8to9 : AutoMigrationSpec