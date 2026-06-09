package com.example.primebus.data.repository

import com.example.primebus.AppResult
import com.example.primebus.NetworkMonitor
import com.example.primebus.data.models.Bus
import com.example.primebus.core.di.quantifiers.BusRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BusRepository @Inject constructor(
    @BusRef private val busRef: DatabaseReference,
    private val networkMonitor: NetworkMonitor
) {

    fun getBusesByRoute(routeId: String): Flow<AppResult<List<Bus>>> = callbackFlow {

        if (!networkMonitor.isOnline.value) {
            trySend(AppResult.NoInternet)
            close()
            return@callbackFlow
        }

        trySend(AppResult.Loading)

        val query = busRef.orderByChild("routeId").equalTo(routeId)

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val buses = snapshot.children.mapNotNull {
                    it.getValue(Bus::class.java)
                }
                trySend(
                    AppResult.Success(buses)
                )
            }

            override fun onCancelled(error: DatabaseError) {

                trySend(
                    AppResult.Error(error.message)
                )
                close()
            }
        }

        query.addValueEventListener(listener)

        awaitClose {
            query.removeEventListener(listener)
        }
    }

    suspend fun getBusById(busId: String): Bus? {
        return try {
            busRef.child(busId)
                .get()
                .await()
                .getValue(Bus::class.java)
        } catch (e: Exception) {
            null
        }
    }
}