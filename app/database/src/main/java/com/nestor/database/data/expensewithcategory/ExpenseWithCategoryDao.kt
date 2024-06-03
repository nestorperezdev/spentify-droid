package com.nestor.database.data.expensewithcategory

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseWithCategoryDao {
    @Transaction
    @Query(
        "SELECT * FROM expense WHERE expense.date BETWEEN :from AND :to AND expense.user_uuid = :userUuid AND expense.stored_at > :expirationDate AND expense.currency_code = :currencyCode ORDER BY expense.cursor DESC"
    )
    fun getExpensesWithCategory(
        from: Date,
        to: Date,
        userUuid: String,
        expirationDate: Date,
        currencyCode: String
    ): Flow<List<ExpenseWithCategoryEntity>>
}