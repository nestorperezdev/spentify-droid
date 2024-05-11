package com.nestor.expenses.data.model

import java.util.Date

data class ExpenseEntity(
    val id: String,
    val amount: Double,
    val usdValue: Double,
    val description: String,
    val date: Date,
    val cursor: Int
)
