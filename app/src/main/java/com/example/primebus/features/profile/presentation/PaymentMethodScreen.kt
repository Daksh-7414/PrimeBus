package com.example.primebus.features.profile.presentation

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.primebus.R
import com.example.primebus.ui.theme.gradientBrush

@Preview
@Composable
private fun PaymentMethodScreenPreview() {
    val navController = rememberNavController()
    PaymentMethodScreen(
        navController = navController
    )
}

@Composable
fun PaymentMethodScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F3F9))
    ) {
        PrivacyToolBar(
            title = "Payment Method",
            onBackClick={navController.popBackStack()}
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                PaymentHeaderCard()
            }
            item {
                Text(
                    text = "Add Payment Method",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaymentOptions(
                        "Add Debit/Credit Card",
                        R.drawable.add_card,
                        modifier = Modifier.weight(1f)
                    )
                    PaymentOptions(
                        "Add UPI ID",
                        R.drawable.qr_code,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaymentOptions(
                        "Add Net Banking",
                        R.drawable.net_banking_icon,
                        modifier = Modifier.weight(1f)
                    )
                    PaymentOptions(
                        "Link Wallet",
                        R.drawable.link_icon,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(Color(0xFFE8EBF3), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.security_icon),
                                contentDescription = null,
                                tint = Color(0xFF00236F),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column() {
                            Text(
                                text = "Secure Payments",
                                fontSize = 17.sp,
                                color = Color(0xFF012470),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "All transactions are encrypted and securely processed.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        )
        {
            Button(
                onClick = { },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = gradientBrush,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentPadding = PaddingValues( vertical = 16.dp)
            ){
                Text(
                    "Add Payment Method",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PaymentHeaderCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Manage Your Payments",
            fontSize = 18.sp,
            color = Color(0xFF012470),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Save payment options for faster and \nsecure checkout",
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}

@Composable
fun PaymentOptions(
    title: String,
    icon: Int,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ){
        Icon(
            painter = painterResource(icon),
            contentDescription = "Icon",
            tint = Color(0xFF00236F),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}