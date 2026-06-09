package com.example.primebus.features.booking.presentation

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsBusFilled
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.Wallet
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.primebus.R
import com.example.primebus.core.navigation.appnavigation.NavGraph
import com.example.primebus.core.navigation.appnavigation.NavRoutes
import com.example.primebus.features.profile.presentation.PrivacyToolBar

@Preview
@Composable
private fun BookingSuccessScreenPreview() {
    val navController = rememberNavController()
    BookingSuccessScreen(navController = navController)
}

@Composable
fun BookingSuccessScreen(navController: NavHostController) {

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F8FE))
    ) {
        PrivacyToolBar(
            title = "Booked Confirmed",
            onBackClick={navController.popBackStack()}
        )
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color(0xFFE0FFF1), CircleShape),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Icon(
                            painter = painterResource(R.drawable.confirmed_icon),
                            contentDescription = "Icon",
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFF059669)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Booking Confirmed 🎉",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Your trip has been successfully booked. Get ready for your journey.",
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }

            }
            item {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))   // rounds the corners
                        .background(Color.White)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)// white background
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Box(
                            modifier = Modifier
                                .size(43.dp)
                                .background(Color(0xFFF3F3F3), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Payments,
                                contentDescription = "Icon",
                                tint = Color(0xFF1E3A8A),
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(start = 10.dp)
                        ) {
                            Text(
                                text = "Payment Successful",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Text(
                                text = "Transaction ID: 1234567890",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                color = Color.DarkGray
                            )
                        }
                        Icon(
                            painter = painterResource(R.drawable.confirmed),
                            contentDescription = "Icon",
                            tint = Color(0xFF1E3A8A),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Box(
                            modifier = Modifier
                                .size(43.dp)
                                .background(Color(0xFFF3F3F3), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.EventSeat,
                                contentDescription = "Icon",
                                tint = Color(0xFF1E3A8A),
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(start = 10.dp)
                        ) {
                            Text(
                                text = "Seat Reserved",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Text(
                                text = "Seat No: L1",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                color = Color.DarkGray
                            )
                        }
                        Icon(
                            painter = painterResource(R.drawable.confirmed),
                            contentDescription = "Icon",
                            tint = Color(0xFF1E3A8A),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Row 1: Bus name + Confirm Status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Operator",
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                                Text(
                                    text = "Rajasthan Express",
                                    fontSize = 18.sp,
                                    color = Color(0xFF00236F),
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                            }
                            Row(
                                modifier = Modifier.wrapContentWidth()
                                    .background(Color(0xFF1E3A8A), RoundedCornerShape(16.dp))
                                    .padding(horizontal = 14.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.End
                            ) {

                                Text(
                                    text = "2 Seats",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )

                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Row 2: Dropping & Boarding Details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically   // ← align all children vertically
                        )
                        {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Ajmer",
                                    fontSize = 17.sp,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                                Spacer(modifier = Modifier.height(2.dp))   // optional spacing
                                Text(
                                    text = "08:30 AM",
                                    fontSize = 18.sp,
                                    color = Color(0xFF00236F),
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.DirectionsBusFilled,
                                    contentDescription = "Icon",
                                    tint = Color(0xFF712AE2),
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "24 Mar 2026",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "Jaipur",
                                    fontSize = 17.sp,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "10:45 AM",
                                    fontSize = 18.sp,
                                    color = Color(0xFF00236F),
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                            }
                        }
                    }
                }
            }
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate(NavRoutes.TicketCard.route)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF1E3A8A)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(16.dp)
                    )
                    {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.RemoveRedEye,
                                contentDescription = "Icon",
                                tint = Color(0xFF1E3A8A),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                "View Ticket",
                                fontSize = 13.sp,
                                color = Color(0xFF00236F),
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                        }
                    }
                    Button(
                        onClick = {
                            navController.popBackStack(
                                route = NavGraph.BOOKING,
                                inclusive = true
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF1E3A8A)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(16.dp)
                    )
                    {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Home,
                                contentDescription = "Icon",
                                tint = Color(0xFF1E3A8A),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                "Back Home",
                                fontSize = 13.sp,
                                color = Color(0xFF00236F),
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                        }
                    }
                    Button(
                        onClick = {
                            navController.navigate(NavGraph.TRIPS)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF1E3A8A)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(16.dp)
                    )
                    {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Wallet,
                                contentDescription = "Icon",
                                tint = Color(0xFF1E3A8A),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                "My Trips",
                                fontSize = 13.sp,
                                color = Color(0xFF00236F),
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                        }
                    }
                }
            }
        }
    }
}