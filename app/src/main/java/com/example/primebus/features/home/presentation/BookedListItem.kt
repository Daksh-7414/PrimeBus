package com.example.primebus.features.home.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsBusFilled
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.DirectionsBusFilled
import androidx.compose.material.icons.rounded.Refresh
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.primebus.R
import com.example.primebus.data.models.BookedTripUiModel
import com.example.primebus.data.models.Booking
import com.example.primebus.data.models.Bus

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
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = booking.busName,
                        fontSize = 20.sp,
                        color = Color(0xFF00236F),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
                Row(
                    modifier = Modifier.weight(0.5f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Column(horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .background(Color(0xFFECFDF5), RoundedCornerShape(20.dp))
                            .border(BorderStroke(1.dp, Color(0xFF16A34A)), RoundedCornerShape(20.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = booking.status,
                            fontSize = 13.sp,
                            color = Color(0xFF16A34A),
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bus.source,
                        fontSize = 17.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = bus.departureTime,
                        fontSize = 15.sp,
                        color = Color(0xFF00236F),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = bus.duration,
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
                        text = bus.destination,
                        fontSize = 17.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = bus.arrivalTime,
                        fontSize = 15.sp,
                        color = Color(0xFF00236F),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier =  Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DirectionsBusFilled,
                        contentDescription = "Icon",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(20.dp).align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = bus.type,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.seat_icon),
                        contentDescription = "Seat Icon",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${booking.passengers.size} Passengers",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
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
                ) {
                    Text(
                        text = "Track Bus",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00236F),
                        fontFamily = FontFamily(Font(R.font.inter)),
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {onViewSeatsClick()},
                    modifier = Modifier
                        .height(45.dp).weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3D3BC4)
                    )
                ) {
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
            seats = listOf("L1", "L2"),
            totalAmount = 1598.0,
            timestamp = System.currentTimeMillis(),
            journeyDate = System.currentTimeMillis()
            ),
        bus = Bus(
            busId = "BUS1",
            busName = "Rajasthan Express",
            type = "AC Sleeper",
            rating = 4.2,
            price = 799.0,
            departureTime = "08:00 AM",
            arrivalTime = "10:30 AM",
            duration = "2H 30M",
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