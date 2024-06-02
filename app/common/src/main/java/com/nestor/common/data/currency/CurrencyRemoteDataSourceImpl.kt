package com.nestor.common.data.currency

import com.apollographql.apollo3.ApolloClient
import com.nestor.schema.CurrencyInfoQuery
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.safeApiCall
import javax.inject.Inject

class CurrencyRemoteDataSourceImpl @Inject constructor(private val client: ApolloClient) :
    CurrencyRemoteDataSource {
    override suspend fun fetchCurrencies(): com.nestor.schema.utils.ResponseWrapper<CurrencyInfoQuery.Data> {
        return com.nestor.schema.utils.safeApiCall { client.query(CurrencyInfoQuery()).execute() }
    }
}