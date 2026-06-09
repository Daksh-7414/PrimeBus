package com.example.primebus.features.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.primebus.data.models.Booking
import com.example.primebus.data.models.Bus

// ─── FIX: Bus added to entities list — Room now creates the buses table.
// Version bumped to 2. Add a Migration(1, 2) if you need to preserve existing
// data on user devices; for a fresh install wipe/recreate is fine.
@Database(
    entities = [Booking::class, Bus::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(BookingTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
}
