package com.example.primebus.features.booking.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.DirectionsBusFilled
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.primebus.R
import com.example.primebus.data.models.Booking
import com.example.primebus.data.models.Bus
import com.example.primebus.data.models.Passenger
import com.example.primebus.features.booking.viewmodels.BookedTripsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun BusTicketCard(
    viewModel: BookedTripsViewModel,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    val trip by viewModel.selectedTrip.collectAsState()
    
    trip?.let { bookedTrip ->
        BusTicketCardContent(
            bus = bookedTrip.bus,
            booking = bookedTrip.booking,
            onBackClick={navController.popBackStack()}
        )
    }

}

@Composable
fun BusTicketCardContent(bus: Bus, booking: Booking,onBackClick: () -> Unit,) {
    Column()
    {
        BookedToolbar("My Ticket",onBackClick,{})
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color(0xFFD1FAE5), CircleShape),
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
                            "Booking Confirmed",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 19.sp,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Your ticket has been successfully booked",
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    )
                    {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Bus name,type

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            )
                            {
                                Column {
                                    Text(
                                        text = bus.busName,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF00236F),
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = bus.type,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier
                                        .background(Color(0xFFECFDF5), RoundedCornerShape(20.dp))
                                        .border(
                                            BorderStroke(1.dp, Color(0xFF8EFFB0)),
                                            RoundedCornerShape(20.dp)
                                        )
                                        .padding(6.dp)
                                ) {
                                    Text(
                                        text = "CONFIRMED",
                                        fontSize = 13.sp,
                                        color = Color(0xFF16A34A),
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                }
                            }

                            // Route
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Column(
                                    horizontalAlignment = Alignment.Start,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        "FROM",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.DarkGray,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                    Text(
                                        bus.source,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.DarkGray,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .wrapContentWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        DashedHorizontalLine()
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = Icons.Outlined.DirectionsBusFilled,
                                            contentDescription = "Icon",
                                            tint = Color(0xFF712AE2),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        DashedHorizontalLine()
                                    }
                                }
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        "TO",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                    Text(
                                        bus.destination,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.DarkGray,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Date
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFFEEF4FC))
                                    .padding(10.dp, 6.dp)
                            )
                            {
                                Text(
                                    text = booking.journeyDate?.let { millis ->
                                        SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH).apply {
                                            timeZone = TimeZone.getTimeZone("UTC")
                                        }.format(Date(millis))
                                    } ?: "Date not available",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontFamily = FontFamily(Font(R.font.inter))
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Departure & Arrival & Duration
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFFEEF4FC))
                                    .padding(10.dp)
                            )
                            {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "DEPARTURE",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                    Text(
                                        text = bus.departureTime,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00236F),
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                }
                                VerticalDivider(
                                    modifier = Modifier.height(50.dp).background(Color.LightGray)
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "DURATION",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                    Text(
                                        bus.duration,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                }
                                VerticalDivider(
                                    modifier = Modifier.height(50.dp).background(Color.LightGray)
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "ARRIVAL",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                    Text(
                                        bus.arrivalTime,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF712AE2),
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // ----- Dashed Divider (simple full width) -----
                            DashedDivider()

                            Spacer(modifier = Modifier.height(8.dp))

//                RouteTimeline()
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Spacer(modifier = Modifier.width(12.dp))
                                RouteTimeline()
                                Column() {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp, 16.dp, 16.dp, 16.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(Color(0xFFEEF4FC))
                                            .padding(16.dp, 10.dp, 10.dp, 10.dp)
                                    )
                                    {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                "BOARDING POINT • ${bus.departureTime} REPORTING",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF00236F),
                                                fontFamily = FontFamily(Font(R.font.inter))
                                            )
                                            Text(
                                                bus.boardingPoint,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                fontFamily = FontFamily(Font(R.font.inter))
                                            )
                                        }
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 12.dp)
                                            .padding(end = 16.dp)
                                            .padding(top = 4.dp)
                                            .padding(bottom = 20.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(Color(0xFFEEF4FC))
                                            .padding(16.dp, 10.dp, 10.dp, 10.dp)
                                    )
                                    {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                "DROPPING POINT • ${bus.arrivalTime} ARRIVAL",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF712AE2),
                                                fontFamily = FontFamily(Font(R.font.inter))
                                            )
                                            Text(
                                                bus.droppingPoint,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                fontFamily = FontFamily(Font(R.font.inter))
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }

                    PassengerDetails(booking.passengers)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(Color(0xFFEEF4FC))
                            .padding(20.dp)
                    )
                    {
                        Text(
                            text = "Fare Summary",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 18.sp,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        )
                        {
                            Text(
                                text = "Base Fare",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray,
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Text(
                                text = "₹${booking.totalAmount}",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold))
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        )
                        {
                            Text(
                                text = "GST & Taxes",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray,
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Text(
                                text = "₹24.00",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold))
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        )
                        {
                            Text(
                                text = "Convenience Fees",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray,
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Text(
                                text = "₹20.00",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold))
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(
                            thickness = 0.6.dp,
                            modifier = Modifier.padding(horizontal = 1.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        )
                        {
                            Text(
                                text = "Total Paid",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Text(
                                text = "₹${booking.totalAmount + 24 + 20}",
                                color = Color(0xFF3F51B5),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold))
                            )
                        }
                    }
            }
        }
    }
}

@Composable
fun RouteTimeline() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top stop
        StopCircle(
            outerColor = Color(0xFFE5E7EB),
            innerColor = Color(0xFF0B3D91)
        )

        DashedVerticalLine()

        // Bottom stop
        StopCircle(
            outerColor = Color(0xFFEDE9FE),
            innerColor = Color(0xFF7C3AED)
        )
    }
}

@Composable
fun StopCircle(
    outerColor: Color,
    innerColor: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(outerColor)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(innerColor)
        )
    }
}

@Composable
fun DashedHorizontalLine() {
    Canvas(
        modifier = Modifier
            .height(2.dp)
            .width(65.dp)
    ) {
        val dashWidth = 10f
        val gap = 6f
        var startX = 0f

        while (startX < size.width) {
            drawLine(
                color = Color.Gray,
                start = Offset(x = startX, y = size.height / 2),
                end = Offset(x = startX + dashWidth, y = size.height / 2),
                strokeWidth = 3f
            )
            startX += dashWidth + gap
        }
    }
}

@Composable
fun DashedVerticalLine() {
    Canvas(
        modifier = Modifier
            .height(55.dp)
            .width(2.dp)
    ) {
        val dashHeight = 10f
        val gap = 6f
        var startY = 0f

        while (startY < size.height) {
            drawLine(
                color = Color.Gray,
                start = Offset(x = size.width / 2, y = startY),
                end = Offset(x = size.width / 2, y = startY + dashHeight),
                strokeWidth = 3f
            )
            startY += dashHeight + gap
        }
    }
}

@Composable
fun BookedToolbar(
    routeString : String,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp)
            .padding(start = 4.dp)
            .padding(end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = routeString,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            fontFamily = FontFamily(Font(R.font.inter))
        )
        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(R.drawable.share_icon),
                contentDescription = "Share",
                modifier = Modifier.size(24.dp)

            )
        }
    }
}

@Composable
fun PassengerDetails(
    passengers: List<Passenger>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {

        Text(
            text = "Passenger Details",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.inter))
        )

        Spacer(modifier = Modifier.height(14.dp))

        passengers.forEachIndexed { index, passenger ->

            PassengeCard(
                index = index,
                passenger = passenger
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


@Composable
fun PassengeCard(
    index: Int,
    passenger: Passenger,
) {

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {

            // 🔹 Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .background(Color(0xFFADEFC6), CircleShape),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        text = "DS",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Column() {
                        Text(
                            text = passenger.name,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Text(
                            text = "${passenger.gender}, ${passenger.age} Yrs",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                    Column(
                        modifier = Modifier.padding(end = 8.dp),
                        horizontalAlignment = Alignment.End) {
                        Text(
                            text = "SEAT NO.",
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Text(
                            text = passenger.seatNumber,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF712ae2),
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
            }
    }
}
// ========== Reusable Components ==========

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFCBD5E1)
) {
    TicketDivider(
        backgroundColor = Color(0xFFF6F9FF) // screen bg color
    )
}

@Composable
fun TicketDivider(
    modifier: Modifier = Modifier,
    circleRadius: Dp = 22.dp,
    dashWidth: Dp = 8.dp,
    dashGap: Dp = 6.dp,
    dividerColor: Color = Color(0xFFE5E7EB),
    backgroundColor: Color = Color.White
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(10.dp)
    ) {
        val radiusPx = circleRadius.toPx()
        val dashWidthPx = dashWidth.toPx()
        val dashGapPx = dashGap.toPx()

        val centerY = size.height / 2

        // Dashed line
        var startX = radiusPx

        while (startX < size.width - radiusPx) {
            drawLine(
                color = dividerColor,
                start = Offset(startX, centerY),
                end = Offset(startX + dashWidthPx, centerY),
                strokeWidth = 4f
            )
            startX += dashWidthPx + dashGapPx
        }

        // Left circular cutout
        drawCircle(
            color = backgroundColor,
            radius = radiusPx,
            center = Offset(0f, centerY)
        )

        // Right circular cutout
        drawCircle(
            color = backgroundColor,
            radius = radiusPx,
            center = Offset(size.width, centerY)
        )
    }
}



// ========== Preview ==========
@Preview(showBackground = true)
@Composable
fun PreviewBusTicketCard() {

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
                name = "Rahul",
                age = "22",
                gender = "Male"
            ),
            Passenger(
                seatId = "2",
                seatNumber = "L2",
                name = "Aman",
                age = "24",
                gender = "Male"
            )
        ),
        totalAmount = 1444.0,
        journeyDate = System.currentTimeMillis(),
        timestamp = System.currentTimeMillis(),
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
        boardingPoint = "Ajmer (Bus Stand)",
        droppingPoint = "Jaipur (Sindhi Camp)"
    )

    MaterialTheme {
        BusTicketCardContent(
            bus = mockBus,
            booking = mockBooking,{}
        )
    }
}