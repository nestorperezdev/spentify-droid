package com.nestor.database.di

import android.content.Context
import androidx.room.Room
import com.nestor.database.SpentifyDatabase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SpentifyDatabaseModule {
    companion object {
        fun providesDatabase(@ApplicationContext context: Context): SpentifyDatabase =
            Room.databaseBuilder(context, SpentifyDatabase::class.java, "spentify-db").build()
    }
}