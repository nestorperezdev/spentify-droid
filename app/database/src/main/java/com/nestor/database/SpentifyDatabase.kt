package com.nestor.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nestor.database.data.catergory.CategoryDao
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.currency.CurrencyDao
import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.database.data.dashboard.DashboardDao
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.database.data.expense.ExpenseDao
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryDao
import com.nestor.database.data.subcategory.SubCategoryEntity
import com.nestor.database.data.subcategory.SubcategoryDao
import com.nestor.database.data.user.UserDao
import com.nestor.database.data.user.UserEntity
import com.nestor.database.migrations.AutoMigration7to8
import com.nestor.database.migrations.AutoMigration9to10
import com.nestor.database.util.DateConverter

@Database(
    entities = [
        DashboardEntity::class,
        CurrencyEntity::class,
        UserEntity::class,
        ExpenseEntity::class,
        CategoryEntity::class,
        SubCategoryEntity::class
    ],
    version = 13,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 7,
            to = 8,
            spec = AutoMigration7to8::class
        ),
        AutoMigration(from = 8, to = 9),
        AutoMigration(
            from = 9,
            to = 10,
            spec = AutoMigration9to10::class
        ),
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13),
    ]
)
@TypeConverters(DateConverter::class)
abstract class SpentifyDatabase : RoomDatabase() {
    abstract fun dashboardDao(): DashboardDao
    abstract fun currencyDao(): CurrencyDao
    abstract fun userDao(): UserDao
    abstract fun expensesDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun subcategoryDao(): SubcategoryDao
    abstract fun expenseWithCategoryDao(): ExpenseWithCategoryDao
}