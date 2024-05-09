package com.nestor.common.data.currency

import com.nestor.database.data.currency.CurrencyEntity
import kotlinx.coroutines.flow.Flow

interface CurrencyLocalDataSource {
    fun fetchCurrencies(): Flow<List<CurrencyEntity>>
    suspend fun clearCurrencies()
    suspend fun saveCurrencies(currencies: List<CurrencyEntity>)
    fun fetchCurrencyByCode(code: String): Flow<CurrencyEntity?>
}