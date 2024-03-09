package com.nestor.database.data.currency

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency")
    fun getAll(): Flow<List<CurrencyEntity>>

    @Insert
    fun insertCurrency(currencyEntity: List<CurrencyEntity>)

    @Query("DELETE FROM currency")
    fun deleteAll(): Int
}