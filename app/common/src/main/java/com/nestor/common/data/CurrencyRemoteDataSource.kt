package com.nestor.common.data

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.CurrencyInfoQuery
import com.nestor.schema.utils.ResponseWrapper

interface CurrencyRemoteDataSource {
    suspend fun fetchCurrencies(): ResponseWrapper<CurrencyInfoQuery.Data>
}