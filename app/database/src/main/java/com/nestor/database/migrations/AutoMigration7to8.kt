package com.nestor.database.migrations

import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec

@RenameTable(fromTableName = "ExpenseEntity", toTableName = "expense")
@RenameColumn(
    tableName = "ExpenseEntity",
    fromColumnName = "cursor",
    toColumnName = "order"
)
class AutoMigration7to8 : AutoMigrationSpec