package com.example.primebus.features.booking.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AirlineSeatReclineExtra
import androidx.compose.material.icons.outlined.AirlineSeatReclineNormal
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DirectionsBusFilled
import androidx.compose.material.icons.rounded.AirlineSeatReclineNormal
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material.icons.rounded.DirectionsBusFilled
import androidx.compose.material.icons.rounded.EventSeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.primebus.R
import com.example.primebus.data.models.BookedTripUiModel
import com.example.primebus.data.models.Booking
import com.example.primebus.data.models.Bus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun BookedListItem(
    booking: Booking,
    bus: Bus,
    onViewSeatsClick: () -> Unit,
    navController: NavController
) {
    Spacer(modifier = Modifier.height(12.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = booking.busName,
                        fontSize = 20.sp,
                        color = Color(0xFF00236F),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Text(
                        text = booking.journeyDate?.let { millis ->
                            SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH).apply {
                                timeZone = TimeZone.getTimeZone("UTC")
                            }.format(Date(millis))
                        } ?: "Date not available",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
                Row(
                    modifier = Modifier.weight(0.5f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Column(horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .background(Color(0xFFDAFBE8), RoundedCornerShape(20.dp))
//                            .border(BorderStroke(1.dp, Color(0xFF16A34A)), RoundedCornerShape(20.dp))
                            .padding(10.dp,5.dp)
                    ) {
                        Text(
                            text = booking.status,
                            fontSize = 12.sp,
                            color = Color(0xFF287145),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Left column – pinned to the left edge
                Column(
                    modifier = Modifier.align(Alignment.CenterStart),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = bus.departureTime,
                        fontSize = 17.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Text(
                        text = bus.source,
                        fontSize = 14.sp,
                        color = Color(0xFF00236F),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }

                // Middle duration indicator – perfectly centered
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DashedHorizontalLine(30.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = bus.duration,
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    DashedHorizontalLine(30.dp)
                }

                // Right column – pinned to the right edge
                Column(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = bus.arrivalTime,
                        fontSize = 17.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Text(
                        text = bus.destination,
                        fontSize = 14.sp,
                        color = Color(0xFF00236F),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Seat Chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .background( Color(0xFFE1E6F2),RoundedCornerShape(12.dp))
                        .padding( horizontal = 10.dp, vertical = 6.dp )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.EventSeat,
                        contentDescription = "Icon",
                        tint = Color(0xFF173578),
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = booking.seats.joinToString(", "),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        color = Color(0xFF173578),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }

                // Bus Type Chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            Color(0xFFF0E7FB),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DirectionsBusFilled,
                        contentDescription = null,
                        tint = Color(0xFF753CDE),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = bus.type,
                        maxLines = 1,
                        fontSize = 13.sp,
                        color = Color(0xFF753CDE),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Fare Chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)

                        .background(
                            Color(0xFFE2F4E9),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF188D3F),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "₹${booking.totalAmount}",
                        maxLines = 1,
                        fontSize = 13.sp,
                        color = Color(0xFF188D3F),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            )
            {
                /*
                Button(
                    onClick = {
                        onViewSeatsClick()
                    },
                    modifier = Modifier
                        .height(45.dp).weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE3E9F1),
                        contentColor = Color.Black
                    )
                )
                {
                    Text(
                        text = "Track Bus",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00236F),
                        fontFamily = FontFamily(Font(R.font.inter)),
                    )
                }
                */
//                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {onViewSeatsClick()},
                    modifier = Modifier
                        .height(40.dp).wrapContentWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3D3BC4)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ConfirmationNumber,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "View Seats",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.inter)),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookedListItemPreview() {

    val navController = rememberNavController()

    val mockTrip = BookedTripUiModel(
        booking = Booking(
            userId = "USER1",
            bookingId = "BK123",
            busName = "Rajasthan Express",
            seats = listOf("L1", "L2","L3","L4"),
            totalAmount = 1598.0,
            timestamp = System.currentTimeMillis(),
            journeyDate = System.currentTimeMillis(),
            journeyDateStr = "Mon, 14 Jun 2026",
        ),
        bus = Bus(
            busId = "BUS1",
            busName = "Rajasthan Express",
            type = "AC Non Sleeper",
            rating = 4.2,
            price = 799.0,
            departureTime = "08:00 AM",
            arrivalTime = "10:30 AM",
            duration = "2h 30m",
            source = "Ajmer",
            destination = "Jaipur",
            boardingPoint = "Ajmer",
            droppingPoint = "Jaipur"
        )
    )

    BookedListItem(
        booking = mockTrip.booking,
        bus = mockTrip.bus,
        onViewSeatsClick = {},
        navController=navController
    )
}