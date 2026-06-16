package com.example.primebus.data.repository

import com.example.primebus.core.di.quantifiers.UserRef
import com.example.primebus.data.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @UserRef private val userRef: DatabaseReference
) {

    fun getCurrentUserEmail(): String {
        return firebaseAuth.currentUser?.email ?: ""
    }

    suspend fun saveUserProfile(userModel: UserModel): Result<Unit> {
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
            val finalUser = userModel.copy(
                userId = uid
            )
            userRef
                .child(uid)
                .setValue(finalUser)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<UserModel> {
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
            val snapshot = userRef
                .child(uid)
                .get()
                .await()
            val user = snapshot.getValue(UserModel::class.java)
            Result.success(
                user ?: UserModel(
                    userId = uid,
                    email = getCurrentUserEmail()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}