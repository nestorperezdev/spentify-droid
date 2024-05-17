package com.nestor.database.migrations

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

@RenameColumn(
    tableName = "expense",
    fromColumnName = "order",
    toColumnName = "cursor"
)
class AutoMigration9to10 : AutoMigrationSpec