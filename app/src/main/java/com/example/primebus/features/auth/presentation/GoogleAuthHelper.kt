package com.example.primebus.features.auth.presentation

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.primebus.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject

class GoogleAuthHelper @Inject constructor() {

    suspend fun getGoogleIdToken(context: Context): String? {

        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(
                context.getString(R.string.default_web_client_id)
            )
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context, request)

        val credential = result.credential

        if (credential is GoogleIdTokenCredential) {
            return credential.idToken
        }

        return null
    }
}