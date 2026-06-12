package com.example.primebus.features.home.presentation

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primebus.R
import com.example.primebus.data.models.Bus

@Composable
fun BusListItem(
    bus: Bus,
    onViewSeatsClick: (Bus) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.weight(2f)
                ) {
                    Column() {
                        Text(
                            text = bus.busName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00236F),
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Row (modifier = Modifier.padding(0.dp,5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = bus.type,
                                fontSize = 13.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20))
                                    .background(Color(0xFFEEF4FC))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            RatingBadge(rating = bus.rating)
                        }
                    }
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Starting from",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "₹${bus.price.toInt()}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00236F),
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = bus.departureTime,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = bus.boardingPoint,
                        fontSize = 15.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
                Row(
                    modifier = Modifier.weight(0.7f).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = bus.duration,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = bus.arrivalTime,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = bus.droppingPoint,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onViewSeatsClick(bus)
                    },
                    modifier = Modifier
                        .height(45.dp).width(125.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3D3BC4)
                    )
                ) {
                    Text(
                        text = "View Seats",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White, // Blue color
                        fontFamily = FontFamily(Font(R.font.inter)),
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBadge(rating: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = "Rating Star",
            tint = Color(0xFF059669),
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = String.format("%.1f", rating),
            fontSize = 14.sp,
            color = Color(0xFF059669),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BusListItemPreview() {
    BusListItem(
        Bus(
            busName = "Rajasthan Express",
            type = "AC Sleeper",
            rating = 4.2,
            price = 799.0,
            departureTime = "10:30 PM",
            arrivalTime = "07:00 AM",
            duration = "8H 30M",
            boardingPoint = "Jaipur (Sindhi Camp)",
            droppingPoint = "Ajmer Bus Stand"
        ),
        onViewSeatsClick = {}
    )
}