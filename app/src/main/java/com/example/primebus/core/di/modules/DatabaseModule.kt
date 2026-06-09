package com.example.primebus.core.di.modules

import android.content.Context
import androidx.room.Room
import com.example.primebus.features.room.AppDatabase
import com.example.primebus.features.room.BookingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "primebus_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideBookingDao(
        database: AppDatabase
    ): BookingDao {

        return database.bookingDao()
    }
}