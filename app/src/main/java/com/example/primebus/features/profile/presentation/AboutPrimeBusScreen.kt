package com.example.primebus.features.profile.presentation

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primebus.R
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
private fun AboutPrimeBusScreenPreview() {
    val navController = rememberNavController()
    AboutPrimeBusScreen(
        navController = navController
    )
}

@Composable
fun AboutPrimeBusScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFE3E9F1))
    ) {
        PrivacyToolBar(
            "About PrimeBus",
            onBackClick = { navController.popBackStack() }
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFEFEFE)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Box( modifier = Modifier
                        .background(Color(0xFFDCE1FF),RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "Bus Icon",
                            modifier = Modifier
                                .padding(20.dp)
                                .size(65.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "PrimeBus",
                        fontSize = 28.sp,
                        color = Color(0xFF012470),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )
                    Text(
                        text = "VERSION V1.1.0",
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFEFEFE))
                        .padding(18.dp),
                    verticalArrangement = Arrangement.Center
                )
                {
                    Text(
                        text = "Our Mission",
                        fontSize = 17.sp,
                        color = Color(0xFF012470),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "At PrimeBus, we are redefining the landscape of intercity travel. Our journey began with a simple belief: that mobility should be fluid, sustainable, and accessible to everyone.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "We leverage kinetic algorithms and real-time fleet management to ensure your journey is not just a commute, but a premium experience from booking to destination.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFFFEFEFE))
                        .padding(18.dp, 16.dp),
                    verticalArrangement = Arrangement.Center
                )
                {
                    Text(
                        text = "Stay Connected",
                        fontSize = 16.sp,
                        color = Color(0xFF012470),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SocialMediaBadge(
                            R.drawable.instagram_icon,
                            "Instagram"
                        )
                        SocialMediaBadge(
                            R.drawable.meta_icon,
                            "Facebook"
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .background(Color(0xFFE3E9F1), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Language,
                                    contentDescription = "Icon",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Website",
                                fontSize = 13.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Thin,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                        }
                    }
                }
                Text(
                    text = "©2026 PrimeBus. All rights reserved.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFB6BDC8),
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }
        }
    }
}

@Composable
fun SocialMediaBadge(icon: Int,lable : String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .background(Color(0xFFE3E9F1),CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Icon",
                tint = Color.Gray,
                modifier = Modifier.size(23.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            lable,
            fontSize = 13.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Thin,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}