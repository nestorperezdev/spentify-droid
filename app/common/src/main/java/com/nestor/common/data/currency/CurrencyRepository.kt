package com.nestor.common.data.currency

import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun fetchCurrencies(): Flow<com.nestor.schema.utils.ResponseWrapper<List<CurrencyEntity>>>
    fun fetchCurrencyByCode(code: String): Flow<com.nestor.schema.utils.ResponseWrapper<CurrencyEntity>>
    suspend fun updateUserCurrency(currency: CurrencyEntity)
}