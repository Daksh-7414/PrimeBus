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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Preview(showBackground = true)
@Composable
private fun CustomerSupportScreenPreview() {
    val navController = rememberNavController()
    CustomerSupportScreen(
        navController = navController
    )}

@Composable
fun CustomerSupportScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F9FF))
    ) {
        PrivacyToolBar(
            title = "Customer Support",
            onBackClick={navController.popBackStack()}
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 0.dp)
                        .background(
                            brush = gradientBrush,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp,18.dp)
                    ) {
                        Text(
                            text = "Need assistance with your booking?",
                            fontSize = 17.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Get instant help from our dedicated support team 24/7.",
                            fontSize = 14.sp,
                            color = Color(0xFFD7CCFF),
                            fontWeight = FontWeight.Thin,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)  // Space between buttons
                        ) {
                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White, // light green background
                                    contentColor = Color(0xFF3D3BC4)    // icon + text color
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
                            )
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.chat_icon),
                                    contentDescription = "Seat Icon",
                                    tint = Color(0xFF00236F),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Chat Now",
                                    fontSize = 14.sp,
                                    color = Color(0xFF3D3BC4),
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                            }

                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1.1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF7354CD), // light green background
                                    contentColor = Color(0xFF3D3BC4)    // icon + text color
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
                            )
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.phone_icon),
                                    contentDescription = "Seat Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Call Support",
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                            }
                        }
                    }
                }
            }
            item {
                Column {
                    Text(
                        text = "Refund Processing Timeline",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    SupportTypes(
                        "Live Chat",
                        "Online - Instant Response",
                        R.drawable.live_chat_icon,
                        Color(0xFF712AE2)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SupportTypes(
                        "Voice Call",
                        "Available 24/7",
                        R.drawable.contact_support_icon,
                        Color(0xFF00236F)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SupportTypes(
                        "WhatsApp",
                        "Avg. response 5 mins",
                        R.drawable.whatsapp_icon,
                        Color(0xFF16a365)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SupportTypes(
                        "Email Support",
                        "Response within 2 hours",
                        R.drawable.mail_icon,
                        Color(0xFF444651)
                    )
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 0.dp)
                        .background(
                            Color(0xFFFFDAD6),
                            shape = RoundedCornerShape(25.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(0.8f)
                        ){
                            Text(
                                text = "Emergency Help",
                                fontSize = 16.sp,
                                color = Color(0xFF93000A),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Text(
                                text = "Bus not arrived or safety concern?",
                                fontSize = 13.sp,
                                color = Color(0xFFC6415E),
                                fontWeight = FontWeight.Thin,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(0.7f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFBA1A1A), // light green background
                                contentColor = Color.White    // icon + text color
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
                        )
                        {
                            Text(
                                text = "SOS Contact",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SupportTypes(
    title: String,
    subtitle: String,
    icon: Int,
    iconColor: Color
){
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))   // rounds the corners
            .background(Color.White)            // white background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(Color(0xFFE3E9F1), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Icon",
                    tint = iconColor,
                    modifier = Modifier.size(21.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    color = Color.DarkGray
                )
            }
        }
    }
}