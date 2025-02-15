package com.nestor.common.data.currency

import com.nestor.common.data.auth.datasource.AuthLocalDataSource
import com.nestor.common.data.auth.datasource.AuthRemoteDataSource
import com.nestor.common.util.parseISODate
import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val localDataSource: CurrencyLocalDataSource,
    private val remoteDataSource: CurrencyRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : CurrencyRepository {
    override fun fetchCurrencies(): Flow<ResponseWrapper<List<CurrencyEntity>>> =
        localDataSource.fetchCurrencies().map { currencies ->
            currencies.ifEmpty { null }?.let {
                ResponseWrapper.success(it)
            } ?: run {
                updateCurrencies()
                ResponseWrapper.loading()
            }
        }

    override fun fetchCurrencyByCode(code: String): Flow<ResponseWrapper<CurrencyEntity>> =
        localDataSource.fetchCurrencyByCode(code).map { currency ->
            currency?.let {
                ResponseWrapper.success(it)
            } ?: run {
                updateCurrencies()
                ResponseWrapper.loading()
            }
        }

    override suspend fun updateUserCurrency(currency: CurrencyEntity) {
        with(currency) {
            authLocalDataSource.updateUserCurrency(code)
            authRemoteDataSource.updateUserCurrency(code)
        }
    }

    private suspend fun updateCurrencies() {
        val currencies = remoteDataSource.fetchCurrencies()
        currencies.body?.let {
            localDataSource.clearCurrencies()
            localDataSource.saveCurrencies(it.currencyInfo.info.map { currencyExchange ->
                CurrencyEntity(
                    code = currencyExchange.currencyInfo.code,
                    name = currencyExchange.currencyInfo.name,
                    lastUpdate = parseISODate(currencyExchange.currencyInfo.lastUpdate) ?: Date(),
                    symbol = currencyExchange.currencyInfo.symbol,
                    usdRate = currencyExchange.currencyInfo.usdRate,
                    exchangeId = currencyExchange.currencyInfo.exchangeId
                )
            })
        }
    }
}