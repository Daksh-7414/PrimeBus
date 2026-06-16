package com.example.primebus.features.auth.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primebus.R
import com.example.primebus.features.auth.viewmodels.AuthState
import com.example.primebus.features.auth.viewmodels.AuthViewModel
import com.example.primebus.features.home.presentation.AppLoadingScreen
import com.example.primebus.ui.theme.gradientBrush
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreenContent(onGoogleClick = {}, onContinuePhone = {})
}

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    googleAuthHelper: GoogleAuthHelper,
    onLoginSuccess: () -> Unit,
    onNavigateToOtp: (String) -> Unit
) {

    val state by authViewModel.state.collectAsState()
    val context = LocalContext.current

    LoginScreenContent(
        state = state,
        onGoogleClick = {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val idToken = googleAuthHelper.getGoogleIdToken(context)
                    if (idToken != null) {
                        authViewModel.signInWithGoogle(idToken)
                    }
                } catch (e: Exception) {
                    Log.d("Auth", e.message ?: "Error")
                }
            }
        },
        onContinuePhone = { phone ->
            authViewModel.sendOtp(phone)
            onNavigateToOtp(phone)
        }
    )

    LaunchedEffect(state) {
        when (state) {
            is AuthState.Loading -> {
//                AppLoadingScreen("Logging...")
            }

            is AuthState.Success -> {
                onLoginSuccess()
                authViewModel.resetState()
            }

            is AuthState.Error -> {
                val message = (state as AuthState.Error).message
                Log.d("Auth", message)
            }

            else -> Unit
        }
    }
}
@Composable
fun LoginScreenContent(
    state: AuthState = AuthState.Idle,
    onGoogleClick: () -> Unit = {},
    onContinuePhone: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {

            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Bus Image",
                modifier = Modifier
                    .padding(top = 16.dp, start = 28.dp,bottom = 16.dp)
                    .size(140.dp)
            )

            Text(
                text = "Welcome to PrimeBus",
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter))

            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Book buses easily across cities",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.inter))

            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
            ){
                PhoneNumberUI(
                    onGoogleClick = onGoogleClick,
                    onContinuePhone = onContinuePhone
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "By continuing, you agree to PrimeBus Terms \n of Service & Privacy Policy",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "PrimeBus",
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp),
                fontFamily = FontFamily(Font(R.font.inter))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "© 2026 PrimeBus Kinetic Gallery",
                fontSize = 15.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
fun PhoneNumberUI(
    onGoogleClick: () -> Unit,
    onContinuePhone: (String) -> Unit
) {
    var phone by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "Mobile Number",
            fontSize = 14.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(R.font.inter))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        )
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(55.dp)
                    .weight(0.2f)
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            ){
                Text(
                    text = "+91 ",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            PhoneNumberInputField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = "Enter mobile number",
                keyboardType = KeyboardType.Phone
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (phone.isBlank()) {
                    Toast.makeText(
                        context,
                        "Enter phone number",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }
                if (phone.length != 10) {
                    Toast.makeText(
                        context,
                        "Phone number should be 10 digits",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                onContinuePhone(phone)
            },
            modifier = Modifier.height(50.dp).fillMaxWidth()
                .background(gradientBrush, shape = RoundedCornerShape(13.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        )
        {
            Text(
                "Continue with Phone",
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        )
        {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 0.6.dp
            )
            Text(
                text = "OR",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter)),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 0.6.dp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onGoogleClick,
            modifier = Modifier.height(50.dp).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )
        )
        {
            Image(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Google Icon",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Continue with Google",
                fontSize = 15.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter))
            )
        }
    }
}

@Composable
fun PhoneNumberInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, color = Color.Gray)
        },
        modifier = Modifier.fillMaxWidth(0.8f),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F5F9),
            unfocusedContainerColor = Color(0xFFF1F5F9),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}