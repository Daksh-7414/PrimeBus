package com.example.primebus.data.repository

import com.example.primebus.AppResult
import com.example.primebus.NetworkMonitor
import com.example.primebus.core.di.quantifiers.BookingRef
import com.example.primebus.core.di.quantifiers.SeatsRef
import com.example.primebus.core.utils.Constants.LOCK_DURATION_MS
import com.example.primebus.data.models.SeatModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SeatRepository @Inject constructor(
    @SeatsRef private val seatLayoutsRef: DatabaseReference,
    @BookingRef private val bookedRef: DatabaseReference,
    private val networkMonitor: NetworkMonitor
) {

    suspend fun getSeatLayout(busType: String): AppResult<List<SeatModel>> {

        if (!networkMonitor.isOnline.value) {
            return AppResult.NoInternet
        }
        return try {
            val snapshot = seatLayoutsRef
                .child(busType)
                .child("seats")
                .get()
                .await()
            val seats = snapshot.children.mapNotNull { child ->
                val seatNumber = child
                    .child("seatNumber")
                    .getValue(String::class.java)
                    ?: return@mapNotNull null
                SeatModel(
                    seatId = seatNumber,
                    seatNumber = seatNumber,
                    row = child.child("row")
                        .getValue(Int::class.java) ?: 0,
                    column = child.child("column")
                        .getValue(Int::class.java) ?: 0,
                    side = child.child("side")
                        .getValue(String::class.java) ?: ""
                )
            }.sortedWith(
                compareBy(
                    SeatModel::row,
                    SeatModel::column
                )
            )
            AppResult.Success(seats)
        } catch (e: Exception) {
            AppResult.Error(
                e.message ?: "Failed to load seat layout"
            )
        }
    }

    suspend fun getBookedSeatNumbers(busId: String, journeyDateStr: String): AppResult<Set<String>> {

        if (!networkMonitor.isOnline.value) return AppResult.NoInternet
        return try {
            val busIdDate = "${busId}_${journeyDateStr}"
            val snapshot  = bookedRef.get().await()
            val bookedSeats = mutableSetOf<String>()
            for (userNode in snapshot.children) {
                for (bookingNode in userNode.children) {
                    val status = bookingNode.child("status").getValue(String::class.java)
                    val bookingBusDate = bookingNode.child("busId_date").getValue(String::class.java)
                    if (status == "CONFIRMED" && bookingBusDate == busIdDate) {
                        for (seatChild in bookingNode.child("seats").children) {
                            seatChild.getValue(String::class.java)?.let { bookedSeats.add(it) }
                        }
                    }
                }
            }
            AppResult.Success(bookedSeats)
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Failed to load booked seats")
        }
    }

    fun observeLockedSeats(
        busId: String,
        journeyDateStr: String,
        currentUserId: String
    ): Flow<Set<String>> = callbackFlow {

        val locksRef = seatLayoutsRef          // @RootRef → root
            .child("seatLocks")         // → seatLocks/
            .child(busId)               // → seatLocks/bus_101/
            .child(journeyDateStr)      // → seatLocks/bus_101/2026-06-10/
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val now    = System.currentTimeMillis()
                val locked = mutableSetOf<String>()
                for (child in snapshot.children) {
                    val lockedBy = child.child("lockedBy").getValue(String::class.java) ?: ""
                    val lockExpiryTime = child.child("lockExpiryTime").getValue(Long::class.java) ?: 0L
                    val seatNumber = child.child("seatNumber").getValue(String::class.java)
                        ?: child.key ?: ""
                    if (lockedBy != currentUserId && lockExpiryTime > now) {
                        locked.add(seatNumber)
                    }
                }
                trySend(locked)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        locksRef.addValueEventListener(listener)
        awaitClose { locksRef.removeEventListener(listener) }
    }

    suspend fun lockSeat(
        busId: String,
        journeyDateStr: String,
        seatNumber: String,
        userId: String
    ): Result<Unit> = try {
        seatLayoutsRef
            .child("seatLocks")
            .child(busId)
            .child(journeyDateStr)
            .child(seatNumber)
            .setValue(mapOf(
                "lockedBy" to userId,
                "lockExpiryTime" to System.currentTimeMillis() + LOCK_DURATION_MS,
                "seatNumber" to seatNumber
            )).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun releaseSeatLock(
        busId: String,
        journeyDateStr: String,
        seatNumber: String
    ): Result<Unit> = try {
        seatLayoutsRef
            .child("seatLocks")
            .child(busId)
            .child(journeyDateStr)
            .child(seatNumber)
            .removeValue()
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun releaseAllLocks(
        busId: String, journeyDateStr: String, seatNumbers: List<String>
    ) {
        try {
            if (seatNumbers.isEmpty()) return
            val updates = seatNumbers.associate {
                "seatLocks/$busId/$journeyDateStr/$it" to null
            }
            seatLayoutsRef.updateChildren(updates as Map<String, Any?>).await()
        } catch (e: Exception) {}
    }
}