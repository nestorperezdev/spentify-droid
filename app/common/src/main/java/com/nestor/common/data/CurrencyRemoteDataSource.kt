package com.nestor.common.data

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.CurrencyInfoQuery

interface CurrencyRemoteDataSource {
    suspend fun fetchCurrencies(): ApolloResponse<CurrencyInfoQuery.Data>
}