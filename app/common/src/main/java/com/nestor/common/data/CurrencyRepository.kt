package com.nestor.common.data

import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun fetchCurrencies(): Flow<ResponseWrapper<List<CurrencyEntity>>>
}