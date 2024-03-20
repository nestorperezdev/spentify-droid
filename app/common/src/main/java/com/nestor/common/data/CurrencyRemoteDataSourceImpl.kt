package com.nestor.common.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.CurrencyInfoQuery
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.safeApiCall
import javax.inject.Inject

class CurrencyRemoteDataSourceImpl @Inject constructor(private val client: ApolloClient) :
    CurrencyRemoteDataSource {
    override suspend fun fetchCurrencies(): ResponseWrapper<CurrencyInfoQuery.Data> {
        return safeApiCall { client.query(CurrencyInfoQuery()).execute() }
    }
}