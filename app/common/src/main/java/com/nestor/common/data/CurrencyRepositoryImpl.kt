package com.nestor.common.data

import com.nestor.common.util.parseISODate
import com.nestor.database.data.currency.CurrencyEntity
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val localDataSource: CurrencyLocalDataSource,
    private val remoteDataSource: CurrencyRemoteDataSource
) : CurrencyRepository {
    override fun fetchCurrencies(): Flow<ResponseWrapper<List<CurrencyEntity>>> = flow {
        localDataSource.fetchCurrencies().collect { currencies ->
            if (currencies.isEmpty()) {
                emit(ResponseWrapper.loading())
                try {
                    updateCurrencies()
                } catch (e: Exception) {
                    emit(ResponseWrapper.error(e.message ?: "Unknown error"))
                }
            } else {
                emit(ResponseWrapper.success(currencies))
            }
        }
    }

    override fun fetchCurrencyByCode(code: String): Flow<ResponseWrapper<CurrencyEntity>> = flow {
        localDataSource.fetchCurrencyByCode(code).collect { currency ->
            currency?.let {
                emit(ResponseWrapper.success(it))
            } ?: run {
                emit(ResponseWrapper.loading())
                try {
                    updateCurrencies()
                } catch (e: Exception) {
                    emit(ResponseWrapper.error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    private suspend fun updateCurrencies() {
        val currencies = remoteDataSource.fetchCurrencies()
        currencies.data?.let {
            localDataSource.clearCurrencies()
            localDataSource.saveCurrencies(it.currencyInfo.info.map { currencyExchange ->
                CurrencyEntity(
                    code = currencyExchange.currencyInfo.code,
                    name = currencyExchange.currencyInfo.name,
                    lastUpdate = parseISODate(currencyExchange.currencyInfo.lastUpdate) ?: Date(),
                    symbol = currencyExchange.currencyInfo.symbol,
                    usdRate = currencyExchange.currencyInfo.usdRate
                )
            })
        } ?: {
            throw Exception("Failed to fetch currencies")
        }
    }
}