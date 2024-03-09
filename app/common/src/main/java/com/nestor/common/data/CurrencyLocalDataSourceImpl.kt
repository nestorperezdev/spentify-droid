package com.nestor.common.data

import com.nestor.database.data.currency.CurrencyDao
import com.nestor.database.data.currency.CurrencyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyLocalDataSourceImpl @Inject constructor(private val dao: CurrencyDao) :
    CurrencyLocalDataSource {
    override fun fetchCurrencies(): Flow<List<CurrencyEntity>> {
        return dao.getAll()
    }

    override suspend fun clearCurrencies() {
        dao.deleteAll()
    }

    override suspend fun saveCurrencies(currencies: List<CurrencyEntity>) {
        dao.insertCurrency(currencies)
    }
}