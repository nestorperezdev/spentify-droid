package com.nestor.common.data.currency

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.CurrencyInfoQuery
import com.nestor.schema.utils.ResponseWrapper

interface CurrencyRemoteDataSource {
    suspend fun fetchCurrencies(): com.nestor.schema.utils.ResponseWrapper<CurrencyInfoQuery.Data>
}