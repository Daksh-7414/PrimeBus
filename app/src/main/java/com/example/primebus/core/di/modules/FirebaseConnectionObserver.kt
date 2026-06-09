package com.example.primebus.core.di.modules

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseConnectionObserver @Inject constructor(
    private val connectionRef: DatabaseReference
) {
    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    init {
        observeConnection()
    }

    private fun observeConnection() {
        connectionRef.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    _isConnected.value =
                        snapshot.getValue(Boolean::class.java) == true
                }

                override fun onCancelled(error: DatabaseError) {
                    _isConnected.value = false
                }
            }
        )
    }
}