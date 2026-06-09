package com.example.primebus.core.di.modules

import com.example.primebus.core.di.quantifiers.BookingRef
import com.example.primebus.core.di.quantifiers.BusRef
import com.example.primebus.core.di.quantifiers.SeatsRef
import com.example.primebus.core.di.quantifiers.UserRef
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Provides
    @Singleton
    fun provideDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    @BusRef
    fun provideBusRef(database: FirebaseDatabase): DatabaseReference {
        return database.getReference("buses")
    }

    @Provides
    @Singleton
    @SeatsRef
    fun provideSeatsRef(database: FirebaseDatabase): DatabaseReference {
        return database.getReference("seatLayouts")
    }

    @Provides
    @Singleton
    @BookingRef
    fun provideBookingRef(database: FirebaseDatabase): DatabaseReference {
        return database.getReference("bookings")
    }

    @Provides
    @Singleton
    fun provideConnectionRef(
        database: FirebaseDatabase
    ): DatabaseReference {
        return database.getReference(".info/connected")
    }

    @Provides
    @Singleton
    @UserRef
    fun provideUsersRef(database: FirebaseDatabase): DatabaseReference {
        return database.getReference("users")
    }
}