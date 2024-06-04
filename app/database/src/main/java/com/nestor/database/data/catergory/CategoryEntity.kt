package com.nestor.database.data.catergory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("category")
data class CategoryEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val icon: String,
    val subcategoryId: String,
) {
    fun iconUrl(baseUrl: String) = "$baseUrl/icon/$icon"
}