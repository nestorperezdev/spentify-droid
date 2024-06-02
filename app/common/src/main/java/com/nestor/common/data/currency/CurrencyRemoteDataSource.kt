package com.nestor.common.data.currency

import com.nestor.schema.CurrencyInfoQuery
import com.nestor.schema.utils.ResponseWrapper

interface CurrencyRemoteDataSource {
    suspend fun fetchCurrencies(): ResponseWrapper<CurrencyInfoQuery.Data>
}