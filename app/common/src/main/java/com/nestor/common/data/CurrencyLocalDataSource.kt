package com.nestor.common.data

import com.nestor.database.data.currency.CurrencyEntity
import kotlinx.coroutines.flow.Flow

interface CurrencyLocalDataSource {
    fun fetchCurrencies(): Flow<List<CurrencyEntity>>
    suspend fun clearCurrencies()
    suspend fun saveCurrencies(currencies: List<CurrencyEntity>)
}