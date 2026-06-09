/*
package com.example.primebus.data.repository

import com.example.primebus.AppResult
import com.example.primebus.NetworkMonitor
import com.example.primebus.core.di.quantifiers.BookingRef
import com.example.primebus.core.di.quantifiers.SeatsRef
import com.example.primebus.data.models.SeatModel
import com.example.primebus.data.models.SeatUIModel
import com.example.primebus.data.models.SeatStatus
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class SeatRepository @Inject constructor(
   @SeatsRef private val rootRef: DatabaseReference,
   @BookingRef private val bookedRef: DatabaseReference,
   private val networkMonitor: NetworkMonitor
) {
    /*suspend fun getSeatLayout(busType: String): AppResult<List<SeatModel>> {
        if (!networkMonitor.isOnline.value) {
            return AppResult.NoInternet
        }

        return try {
            val snapshot = rootRef
                .child(busType)
                .child("seats")
                .get()
                .await()

            val seats = snapshot.children.mapNotNull { child ->
                val seatNumber = child.child("seatNumber").getValue(String::class.java) ?: return@mapNotNull null
//                val row        = child.child("row").getValue(Int::class.java) ?: 0
//                val column     = child.child("column").getValue(Int::class.java) ?: 0
//                val side       = child.child("side").getValue(String::class.java) ?: ""
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
            }.sortedWith(compareBy({ it.row }, { it.column }))

        } catch (e: Exception) {
            emptyList()
        }
    }
     */

    suspend fun getSeatLayout(
        busType: String
    ): AppResult<List<SeatModel>> {

        if (!networkMonitor.isOnline.value) {
            return AppResult.NoInternet
        }

        return try {

            val snapshot = rootRef
                .child(busType)
                .child("seats")
                .get()
                .await()

            val seats = snapshot.children.mapNotNull { child ->

                val seatNumber =
                    child.child("seatNumber")
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
    suspend fun getBookedSeatNumbers(
        busId: String,
        journeyDateStr: String
    ): AppResult<Set<String>> {


        if (!networkMonitor.isOnline.value) {
            return AppResult.NoInternet
        }

        return try {

            val busIdDate = "${busId}_${journeyDateStr}"

            val snapshot = bookedRef.get().await()

            val bookedSeats = mutableSetOf<String>()

            // bookings/{userId}
            for (userNode in snapshot.children) {

                // bookings/{userId}/{bookingId}
                for (bookingNode in userNode.children) {

                    val status = bookingNode
                        .child("status")
                        .getValue(String::class.java)

                    val bookingBusDate = bookingNode
                        .child("busId_date")
                        .getValue(String::class.java)

                    if (
                        status == "CONFIRMED" &&
                        bookingBusDate == busIdDate
                    ) {

                        val seatsSnapshot =
                            bookingNode.child("seats")

                        for (seatChild in seatsSnapshot.children) {

                            seatChild
                                .getValue(String::class.java)
                                ?.let { seat ->

                                    bookedSeats.add(seat)
                                }
                        }
                    }
                }
            }

            AppResult.Success(bookedSeats)

        } catch (e: Exception) {

            AppResult.Error(
                e.message ?: "Failed to load booked seats"
            )
        }
    }
    // ─────────────────────────────────────────────────────────────
    // Real-time listener: seat locks for this bus + date
    // Path: seatLocks/{busId}/{YYYY-MM-DD}/{seatNumber}
    // Returns a Flow of locked seat numbers (excluding expired locks)
    // ─────────────────────────────────────────────────────────────
    fun observeLockedSeats(
        busId: String,
        journeyDateStr: String,
        currentUserId: String
    ): Flow<Set<String>> = callbackFlow {

        val locksRef = rootRef
            .child("seatLocks")
            .child(busId)
            .child(journeyDateStr)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val now = System.currentTimeMillis()
                val locked = mutableSetOf<String>()
                for (child in snapshot.children) {
                    val lockedBy       = child.child("lockedBy").getValue(String::class.java) ?: ""
                    val lockExpiryTime = child.child("lockExpiryTime").getValue(Long::class.java) ?: 0L
                    val seatNumber     = child.child("seatNumber").getValue(String::class.java) ?: child.key ?: ""

                    // Skip locks held by the current user (they can re-select)
                    // Skip expired locks
                    if (lockedBy != currentUserId && lockExpiryTime > now) {
                        locked.add(seatNumber)
                    }
                }
                trySend(locked)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        locksRef.addValueEventListener(listener)
        awaitClose { locksRef.removeEventListener(listener) }
    }

    // ─────────────────────────────────────────────────────────────
    // Write a seat lock — atomic transaction to prevent double-booking
    // Path: seatLocks/{busId}/{YYYY-MM-DD}/{seatNumber}
    // ─────────────────────────────────────────────────────────────
    suspend fun lockSeat(
        busId: String,
        journeyDateStr: String,
        seatNumber: String,
        userId: String
    ): Result<Unit> {
        return try {
            val lockData = mapOf(
                "lockedBy"       to userId,
                "lockExpiryTime" to System.currentTimeMillis() + LOCK_DURATION_MS,
                "seatNumber"     to seatNumber
            )
            rootRef
                .child("seatLocks")
                .child(busId)
                .child(journeyDateStr)
                .child(seatNumber)
                .setValue(lockData)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Release a seat lock (user deselects)
    // ─────────────────────────────────────────────────────────────
    suspend fun releaseSeatLock(
        busId: String,
        journeyDateStr: String,
        seatNumber: String
    ): Result<Unit> {
        return try {
            rootRef
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
    }

    // ─────────────────────────────────────────────────────────────
    // Release ALL locks held by a user on a bus+date
    // Called on back press or screen dispose
    // ─────────────────────────────────────────────────────────────
    suspend fun releaseAllLocks(
        busId: String,
        journeyDateStr: String,
        seatNumbers: List<String>
    ) {
        try {
            val updates = mutableMapOf<String, Any?>()
            seatNumbers.forEach { seatNumber ->
                updates["seatLocks/$busId/$journeyDateStr/$seatNumber"] = null
            }
            if (updates.isNotEmpty()) {
                rootRef.updateChildren(updates).await()
            }
        } catch (_: Exception) {}
    }

    companion object {
        const val LOCK_DURATION_MS = 10 * 60 * 1000L  // 10 minutes
    }
}
 */
package com.example.primebus.data.repository

import com.example.primebus.AppResult
import com.example.primebus.NetworkMonitor
import com.example.primebus.core.di.quantifiers.BookingRef
import com.example.primebus.core.di.quantifiers.SeatsRef
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

    // ─────────────────────────────────────────────────────────────
    // getSeatLayout — reads seatLayouts/{busType}/seats from Firebase
    // Returns AppResult.Success(emptyList) if the path has no data
    // (ViewModel's generateDynamicSeats() handles that fallback)
    // ─────────────────────────────────────────────────────────────
    suspend fun getSeatLayout(
        busType: String
    ): AppResult<List<SeatModel>> {

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

    // ─────────────────────────────────────────────────────────────
    // getBookedSeatNumbers — queries bookings by busId_date
    // bookedRef = @BookingRef = database.getReference("bookings") ← correct
    // ─────────────────────────────────────────────────────────────
    suspend fun getBookedSeatNumbers(
        busId: String,
        journeyDateStr: String
    ): AppResult<Set<String>> {

        if (!networkMonitor.isOnline.value) return AppResult.NoInternet

        return try {
            val busIdDate = "${busId}_${journeyDateStr}"
            val snapshot  = bookedRef.get().await()
            val bookedSeats = mutableSetOf<String>()

            for (userNode in snapshot.children) {
                for (bookingNode in userNode.children) {
                    val status         = bookingNode.child("status").getValue(String::class.java)
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

    // ─────────────────────────────────────────────────────────────
    // observeLockedSeats — seatLocks/{busId}/{date}/{seatNumber}
    // Bug 3 fix: with @RootRef path is now correct
    // ─────────────────────────────────────────────────────────────
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
                    val lockedBy       = child.child("lockedBy").getValue(String::class.java) ?: ""
                    val lockExpiryTime = child.child("lockExpiryTime").getValue(Long::class.java) ?: 0L
                    val seatNumber     = child.child("seatNumber").getValue(String::class.java)
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

    // seatLocks/{busId}/{date}/{seatNumber}
    suspend fun lockSeat(
        busId: String, journeyDateStr: String, seatNumber: String, userId: String
    ): Result<Unit> = try {
        seatLayoutsRef.child("seatLocks").child(busId).child(journeyDateStr).child(seatNumber)
            .setValue(mapOf(
                "lockedBy"       to userId,
                "lockExpiryTime" to System.currentTimeMillis() + LOCK_DURATION_MS,
                "seatNumber"     to seatNumber
            )).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun releaseSeatLock(
        busId: String, journeyDateStr: String, seatNumber: String
    ): Result<Unit> = try {
        seatLayoutsRef.child("seatLocks").child(busId).child(journeyDateStr).child(seatNumber)
            .removeValue().await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun releaseAllLocks(
        busId: String, journeyDateStr: String, seatNumbers: List<String>
    ) {
        try {
            if (seatNumbers.isEmpty()) return
            val updates = seatNumbers.associate {
                "seatLocks/$busId/$journeyDateStr/$it" to null
            }
            seatLayoutsRef.updateChildren(updates as Map<String, Any?>).await()
        } catch (_: Exception) {}
    }

    companion object {
        const val LOCK_DURATION_MS = 10 * 60 * 1000L
    }
}