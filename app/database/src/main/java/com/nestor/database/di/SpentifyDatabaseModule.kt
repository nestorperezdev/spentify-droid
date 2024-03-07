package com.nestor.database.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.nestor.database.SpentifyDatabase
import com.nestor.database.data.dashboard.DashboardDao
import com.nestor.database.data.encryptedpreferences.EncryptedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpentifyDatabaseModule {
    companion object {
        @Singleton
        @Provides
        fun providesDatabase(@ApplicationContext context: Context): SpentifyDatabase =
            Room.databaseBuilder(context, SpentifyDatabase::class.java, "spentify-db").build()

        @Provides
        @Singleton
        @Named(EncryptedPreferences.ENCRYPTED_MASTER_KEY_ALIAS)
        fun providesMasterKeyAlias() = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        @Singleton
        @Provides
        @Named(EncryptedPreferences.ENCRYPTED_PREFERENCES)
        fun provideEncryptedPreferences(
            @ApplicationContext appContext: Context,
            @Named(EncryptedPreferences.ENCRYPTED_MASTER_KEY_ALIAS) masterKeyAlias: String
        ): SharedPreferences {
            return EncryptedSharedPreferences.create(
                "enc_prefs",
                masterKeyAlias,
                appContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        @Singleton
        @Provides
        fun providesDashboardDao(database: SpentifyDatabase): DashboardDao {
            return database.dashboardDao()
        }
    }
}