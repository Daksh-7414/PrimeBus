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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.primebus.R
import com.example.primebus.core.navigation.appnavigation.NavGraph
import com.example.primebus.core.navigation.appnavigation.NavRoutes
import com.example.primebus.data.models.Booking
import com.example.primebus.data.models.Bus
import com.example.primebus.data.models.Passenger
import com.example.primebus.features.home.viewmodels.BookingViewModel
import com.example.primebus.features.profile.presentation.PrivacyToolBar
import com.example.primebus.ui.theme.gradientBrush
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Preview
@Composable
private fun BookingSuccessScreenPreview() {
    val mockBooking = Booking(
        userId = "USER1",
        bookingId = "BK123",
        busId = "BUS1",
        busName = "Rajasthan Express",
        seats = listOf("L1", "L2"),
        passengers = listOf(
            Passenger(
                seatId = "1",
                seatNumber = "L1",
                name = "Rahul Singh",
                age = "22",
                gender = "Male"
            ),
            Passenger(
                seatId = "2",
                seatNumber = "L2",
                name = "Daksh Singh",
                age = "24",
                gender = "Male"
            )
        ),
        totalAmount = 1444.0,
        journeyDate = System.currentTimeMillis(),
        timestamp = System.currentTimeMillis(),
        paymentId = "pay_T0bdIqOykTJYNq"
    )
    val mockBus = Bus(
        busId = "BUS1",
        busName = "Rajasthan Express",
        type = "AC Sleeper",
        rating = 4.2,
        price = 799.0,
        departureTime = "08:00 AM",
        arrivalTime = "10:30 AM",
        duration = "2h 30m",
        source = "Ajmer",
        destination = "Jaipur",
        operator = "RSRTC",
        boardingPoint = "Ajmer (Bus Stand)",
        droppingPoint = "Jaipur (Sindhi Camp)"
    )
    BookingSuccessScreenContent(
        mockBus,
        mockBooking,
        {},
        {},
        {},
        {}
    )
}

@Composable
fun BookingSuccessScreen(
    bookingViewModel: BookingViewModel,
    navController: NavHostController
) {
    val booking by bookingViewModel.booking.collectAsState()
    val bus by bookingViewModel.selectedBus.collectAsState()

    BookingSuccessScreenContent(
        bus = bus,
        booking = booking,
        onBackClick = {
            navController.popBackStack(
                route = NavGraph.BOOKING,
                inclusive = true
            )
        },
        onViewTicketClick = {
            navController.navigate(NavRoutes.TicketCard.route)
        },
        onHomeClick = {
            navController.popBackStack(
                route = NavGraph.BOOKING,
                inclusive = true
            )
        },
        onTripsClick = {
            navController.navigate(NavGraph.TRIPS){
                popUpTo(NavGraph.BOOKING) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
fun BookingSuccessScreenContent(
    bus: Bus?,
    booking: Booking?,
    onBackClick: () -> Unit,
    onViewTicketClick: () -> Unit,
    onHomeClick: () -> Unit,
    onTripsClick: () -> Unit,
) {
    val date = booking?.journeyDate?.let { millis ->
        SimpleDateFormat(
            "EEE, dd MMM yyyy",
            Locale.ENGLISH
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(millis))
    } ?: "Date not available"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8FE))
    ) {
        PrivacyToolBar(
            title = "Booked Confirmed",
            onBackClick={onBackClick}
        )
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                text = "Payment Successful",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Text(
                                text = "Transaction ID: ${booking?.paymentId}",
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
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                text = "Seat Reserved",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Text(
                                text = "Seat No: ${booking?.seats?.joinToString(", ")}",
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
                                    text = bus?.operator ?: "Operator",
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                                Text(
                                    text = bus?.busName ?: "Bus Name",
                                    fontSize = 18.sp,
                                    color = Color(0xFF00236F),
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .background(gradientBrush, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 14.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.End
                            ) {

                                Text(
                                    text = "${booking?.seats?.count().toString()} seats",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )

                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = bus?.source ?: "Ajmer",
                                    fontSize = 17.sp,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = bus?.departureTime ?: "08:00 AM",
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
                                    text = date,
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
                                    bus?.destination ?: "Jaipur",
                                    fontSize = 17.sp,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = bus?.arrivalTime ?: "10:45 AM",
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
                        onClick = { onViewTicketClick },
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
                        onClick = { onHomeClick },
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
                        onClick = { onTripsClick },
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
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray
                            )
                        ) {
                            append("You can view your ticket anytime from ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF00236F),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(" My Trips")
                        }
                        append(".")
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }
        }
    }
}