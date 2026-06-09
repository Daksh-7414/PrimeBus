package com.example.primebus.features.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.primebus.data.models.Booking
import com.example.primebus.data.models.BookedTripUiModel
import com.example.primebus.data.models.Bus
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<Booking>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuses(buses: List<Bus>)

    // ─── @Relation on BookedTripUiModel means Room auto-fetches the Bus
    // for each Booking row. No raw JOIN SQL needed here.
    // @Transaction is REQUIRED with @Relation — ensures both tables are
    // read in the same transaction (prevents torn reads).
    @Transaction
    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun getBookingsWithBus(): Flow<List<BookedTripUiModel>>

    @Query("DELETE FROM bookings")
    suspend fun clearBookings()

    @Query("DELETE FROM buses")
    suspend fun clearBuses()
}