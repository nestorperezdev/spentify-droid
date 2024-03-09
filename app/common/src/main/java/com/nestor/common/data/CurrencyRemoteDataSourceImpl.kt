package com.nestor.common.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.CurrencyInfoQuery
import javax.inject.Inject

class CurrencyRemoteDataSourceImpl @Inject constructor(private val client: ApolloClient) :
    CurrencyRemoteDataSource {
    override suspend fun fetchCurrencies(): ApolloResponse<CurrencyInfoQuery.Data> {
        return client.query(CurrencyInfoQuery()).execute()
    }
}