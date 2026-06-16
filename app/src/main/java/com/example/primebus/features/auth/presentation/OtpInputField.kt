package com.example.primebus.features.auth.presentation

import android.Manifest
import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.primebus.R
import com.example.primebus.core.navigation.appnavigation.NavGraph
import com.example.primebus.features.profile.presentation.PrivacyToolBar
import com.example.primebus.ui.theme.PrimeBusTheme
import com.example.primebus.features.auth.viewmodels.AuthState
import com.example.primebus.features.auth.viewmodels.AuthViewModel
import com.example.primebus.ui.theme.gradientBrush
import kotlinx.coroutines.delay

@Composable
fun OTPScreen(
    navController: NavHostController,
    phone: String,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val authState by authViewModel.state.collectAsState()
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        authViewModel.initOtp(context)
        Log.d("OTP_DEBUG", "Phone = $phone")
        authViewModel.sendOtp(phone)
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate(NavGraph.MAIN) {
                    popUpTo(NavGraph.AUTH) {
                        inclusive = true
                    }
                }
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    OTPScreenContent(
        phone = phone,
        onBackClick = { navController.popBackStack() },
        onVerifyOtp = { otp -> authViewModel.verifyOtp(otp) },
        onResendOtp = { authViewModel.sendOtp(phone) }
    )
}

@Composable
fun OTPScreenContent(
    phone: String,
    onBackClick: () -> Unit,
    onVerifyOtp: (String) -> Unit,
    onResendOtp: () -> Unit
) {
    var otp by remember { mutableStateOf("") }
    val maskedPhone = "XXXXX" + phone.takeLast(5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F3F9))
    ) {
        PrivacyToolBar(
            title = "Verify Phone Number",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Bus Image",
                    modifier = Modifier
                        .padding(top = 16.dp, start = 28.dp, bottom = 16.dp)
                        .size(140.dp)
                )

                Text(
                    text = "Verification Code",
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inter))
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Enter the OTP sent to +91 $maskedPhone",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.inter))
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OtpInputField(
                        numberCharacter = 4,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier,
                        onOtpComplete = { otp = it }
                    )
                    Spacer(modifier = Modifier.height(26.dp))
                    Text(
                        text = "Resend OTP",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFE7E6E6))
                            .clickable { onResendOtp() }
                            .padding(horizontal = 15.dp, vertical = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onVerifyOtp(otp)
                        },
                        enabled = otp.length == 4,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent ),
                        contentPadding = PaddingValues(10.dp,15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background( brush = gradientBrush,
                                shape = RoundedCornerShape(16.dp) )
                    ) {
                        Text(
                            text = "Verify & Continue",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(R.drawable.right_arrow),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
            item {
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
}

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    numberCharacter: Int,
    shape: Shape,
    onOtpComplete: (String) -> Unit
) {
    val otpState = rememberTextFieldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val otpText = otpState.text.toString()

    LaunchedEffect(otpText) {
        onOtpComplete(otpText)
    }

    LaunchedEffect(otpText) {
        if (otpText.length == numberCharacter) {
            delay(100)
            keyboardController?.hide()
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            state = otpState,
            modifier = Modifier.semantics {
                contentType = ContentType.SmsOtpCode
            },
            inputTransformation = InputTransformation.maxLength(numberCharacter),
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorator = {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(numberCharacter) { index ->
                        OtpCharacter(
                            shape = shape,
                            char = otpText.getOrElse(index) { ' ' },
                            focus = index == otpText.length
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun OtpCharacter(
    modifier: Modifier = Modifier,
    shape: Shape,
    char: Char,
    focus: Boolean
) {
    val borderStroke by animateDpAsState(
        targetValue = if (focus) 2.5.dp else 1.5.dp,
        label = "borderStroke"
    )
    val colorStroke by animateColorAsState(
        targetValue = if (focus) Color(0xFF9F8AD4).copy(alpha = 0.8f) else Color.LightGray.copy(alpha = 0.8f),
        label = "colorStroke"
    )
    Box(
        modifier = modifier
            .size(50.dp)
            .clip(shape)
            .border(borderStroke, color = colorStroke, shape = shape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char.toString(),
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OTPScreenPreview() {
    PrimeBusTheme {
        OTPScreenContent(
            phone = "9876543210",
            onBackClick = {},
            onVerifyOtp = {},
            onResendOtp = {}
        )
    }
}
