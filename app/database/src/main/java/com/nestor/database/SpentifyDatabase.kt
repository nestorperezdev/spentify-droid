package com.nestor.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nestor.database.data.currency.CurrencyDao
import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.database.data.dashboard.DashboardDao
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.database.data.expense.ExpenseDao
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.user.UserDao
import com.nestor.database.data.user.UserEntity
import com.nestor.database.util.DateConverter

@Database(
    entities = [DashboardEntity::class, CurrencyEntity::class, UserEntity::class, ExpenseEntity::class],
    version = 7
)
@TypeConverters(DateConverter::class)
abstract class SpentifyDatabase : RoomDatabase() {
    abstract fun dashboardDao(): DashboardDao
    abstract fun currencyDao(): CurrencyDao
    abstract fun userDao(): UserDao
    abstract fun expensesDao(): ExpenseDao
}