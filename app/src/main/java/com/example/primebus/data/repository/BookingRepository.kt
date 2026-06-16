package com.example.primebus.data.repository

import android.util.Log
import com.example.primebus.AppResult
import com.example.primebus.core.di.quantifiers.BookingRef
import com.example.primebus.data.models.Booking
import com.example.primebus.data.models.BookedTripUiModel
import com.example.primebus.data.models.Bus
import com.example.primebus.features.room.BookingDao
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookingRepository @Inject constructor(
    @BookingRef private val rootRef: DatabaseReference,
    private val busRepository: BusRepository,
    private val bookingDao: BookingDao
) {

    suspend fun confirmBooking(userId: String, booking: Booking): Result<String> {
        return try {
            val bookingId = rootRef
                .child(userId)
                .push()
                .key
                ?: return Result.failure(Exception("Could not generate booking ID"))

            val finalBooking = booking.copy(
                bookingId = bookingId,
                userId    = userId
            )

            val updates = mutableMapOf<String, Any?>()
            updates["$userId/$bookingId"] = finalBooking.toMap()

            finalBooking.seats.forEach { seatNumber ->
                updates["seatLocks/${booking.busId}/${booking.journeyDateStr}/$seatNumber"] = null
            }

            rootRef.updateChildren(updates).await()
            Result.success(bookingId)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserBookings(userId: String, isOnline: Boolean
    ): Flow<AppResult<List<BookedTripUiModel>>> = flow {

        emit(AppResult.Loading)
        if (isOnline) {
            CoroutineScope(Dispatchers.IO).launch {
                fetchFromFirebaseAndCache(userId)
            }
        }
        bookingDao
            .getBookingsWithBus()
            .collect { trips ->
                when {
                    trips.isNotEmpty() -> {
                        emit(AppResult.Success(trips))
                    }
                    !isOnline -> {
                        emit(AppResult.NoInternet)
                    }
                    else -> {
                        emit(AppResult.Success(emptyList()))
                    }
                }
            }
    }

    private suspend fun fetchFromFirebaseAndCache(userId: String) {

        val firebaseFlow: Flow<List<Booking>> = callbackFlow {
            val ref = rootRef.child(userId)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookings = snapshot.children
                        .mapNotNull { it.getValue(Booking::class.java) }
                        .filter { it.status == "CONFIRMED" }
                        .sortedByDescending { it.timestamp }
                    Log.d("BookingRepository", "Firebase bookings: $bookings")
                    trySend(bookings)
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            ref.addValueEventListener(listener)
            awaitClose { ref.removeEventListener(listener) }
        }

        firebaseFlow.collect { bookings ->

            val buses: List<Bus> = bookings
                .map { it.busId }
                .distinct()                         // avoid duplicate fetches
                .map { busId ->
                    CoroutineScope(Dispatchers.IO).async {
                        busRepository.getBusById(busId)
                    }
                }
                .awaitAll()
                .filterNotNull()

            bookingDao.insertBuses(buses)
            bookingDao.insertBookings(bookings)
        }
    }

    suspend fun cancelBooking(userId: String, bookingId: String): Result<Unit> {
        return try {
            rootRef
                .child(userId)
                .child(bookingId)
                .child("status")
                .setValue("CANCELLED")
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun Booking.toMap(): Map<String, Any?> = mapOf(
    "bookingId"      to bookingId,
    "userId"         to userId,
    "busId"          to busId,
    "busName"        to busName,
    "routeId"        to routeId,
    "journeyDate"    to journeyDate,
    "journeyDateStr" to journeyDateStr,
    "busId_date"     to busId_date,
    "seats"          to seats,
    "passengers"     to passengers.map { p ->
        mapOf(
            "seatId"     to p.seatId,
            "seatNumber" to p.seatNumber,
            "name"       to p.name,
            "age"        to p.age,
            "gender"     to p.gender
        )
    },
    "totalAmount"    to totalAmount,
    "status"         to status,
    "timestamp"      to timestamp,
    "contactPhone"   to contactPhone,
    "contactEmail"   to contactEmail,
    "paymentId"      to paymentId
)