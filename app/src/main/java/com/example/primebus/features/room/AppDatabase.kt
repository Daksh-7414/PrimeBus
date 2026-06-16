package com.example.primebus.features.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.primebus.data.models.Booking
import com.example.primebus.data.models.Bus

@Database(
    entities = [Booking::class, Bus::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(BookingTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
}
