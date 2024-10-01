package com.nestor.category.data.subcategory

import com.apollographql.apollo.ApolloClient
import com.nestor.schema.CategoryListQuery
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.safeApiCall
import javax.inject.Inject

class SubcategoryRemoteDataSourceImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : SubcategoryRemoteDataSource {
    override suspend fun getCategories(): ResponseWrapper<CategoryListQuery.Data> {
        return safeApiCall {
            apolloClient.query(CategoryListQuery()).execute()
        }
    }
}