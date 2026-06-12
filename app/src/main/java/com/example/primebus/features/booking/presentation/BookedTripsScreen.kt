package com.example.primebus.features.booking.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.primebus.BookedTripsUiState
import com.example.primebus.PullToRefreshLazyColumn
import com.example.primebus.R
import com.example.primebus.core.navigation.appnavigation.NavRoutes
import com.example.primebus.features.booking.viewmodels.BookedTripsViewModel
import com.example.primebus.features.home.presentation.AppLoadingScreen
import com.example.primebus.features.home.presentation.NoInternetCard

@Composable
fun BookedTripsScreen(
    navController: NavHostController,
    viewModel: BookedTripsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        TripToolBar(
            onBackClick = { viewModel.refresh() }
        )

        when (val state = uiState) {

            BookedTripsUiState.Loading -> {
                AppLoadingScreen("Loading trips...")
            }

            BookedTripsUiState.Empty -> {
                PullToRefreshLazyColumn(
                    isRefreshing = isRefreshing,
                    onRefresh    = { viewModel.refresh() },
                    modifier     = Modifier.weight(1f)
                ) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize()) {
                            NoBookedTripsAvailableCard()
                        }
                    }
                }
            }

            BookedTripsUiState.NoInternet -> {
                PullToRefreshLazyColumn(
                    isRefreshing = isRefreshing,
                    onRefresh    = { viewModel.refresh() },
                    modifier     = Modifier.weight(1f)
                ) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize()) {
                            NoInternetCard()
                        }
                    }
                }
            }

            is BookedTripsUiState.Error -> {
                PullToRefreshLazyColumn(
                    isRefreshing = isRefreshing,
                    onRefresh    = { viewModel.refresh() },
                    modifier     = Modifier.weight(1f)
                ) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize()) {
                            // ErrorCard(state.message)
                        }
                    }
                }
            }

            is BookedTripsUiState.Success -> {

//                Column(
//                    modifier = Modifier.fillMaxSize()
//                ) {

                if (state.isOffline) {
                    OfflineBanner()
                }

                PullToRefreshLazyColumn(
                    isRefreshing = isRefreshing,
                    onRefresh    = { viewModel.refresh() },
                    modifier     = Modifier
                        .weight(1f)
                        .background(Color(0xFFE2E8F0))
                ) {
                    items(
                        items = state.trips,
                        key = { it.booking.bookingId }
                    ) { trip ->

                        BookedListItem(
                            booking = trip.booking,
                            bus = trip.bus,
                            navController = navController,
                            onViewSeatsClick = {
                                viewModel.selectTrip(trip)
                                navController.navigate(
                                    NavRoutes.TicketCard.route
                                )
                            }
                        )
                    }
                    item {
                        ContactPage()
                    }
                }
//                }
            }
        }
    }
}

@Composable
fun ContactPage() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Need assistance with your trip?",
                fontSize = 18.sp,
                color = Color(0xFF00236F),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Our support team is available 24/7 to help you with booking modifications or cancellations.",
                fontSize = 15.sp,
                color = Color(0xFF00236F),
                fontWeight = FontWeight.SemiBold,
                lineHeight = 19.sp,
                fontFamily = FontFamily(Font(R.font.inter))
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {},
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE3E9F1),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Contact Support",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF00236F),
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }
        }
    }
}

@Composable
fun NoBookedTripsAvailableCard() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF2F7)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.no_trips),
            contentDescription = "image",
            modifier = Modifier.size(345.dp)
        )
    }
}

@Composable
fun OfflineBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF3CD))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.WifiOff,
            contentDescription = null,
            tint = Color(0xFF856404),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = "You're offline. Showing saved bookings.",
            fontSize = 13.sp,
            color = Color(0xFF856404),
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}

@Composable
fun TripToolBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My bookings",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = "Refresh",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ContactPagePreview() {
    ContactPage()
}

@Preview
@Composable
private fun NoBookedTripsAvailableCardPreview() {
    NoBookedTripsAvailableCard()
}

@Preview
@Composable
private fun OfflineBannerPreview() {
    OfflineBanner()
}

@Preview
@Composable
private fun ToolbarPreview() {
    TripToolBar(onBackClick = {})
}

/*
// ─────────────────────────────────────────────────────────────────────────────
// Empty state — different message when offline vs online
// ─────────────────────────────────────────────────────────────────────────────
//@Composable
//fun EmptyBookingsView(isOffline: Boolean) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFE2E8F0)),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.padding(32.dp)
//        ) {
//            Text(
//                text = if (isOffline) "No cached bookings" else "No bookings yet",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF00236F),
//                fontFamily = FontFamily(Font(R.font.inter))
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = if (isOffline)
//                    "Connect to the internet to load your bookings."
//                else
//                    "Your confirmed trips will appear here.",
//                fontSize = 14.sp,
//                color = Color(0xFF64748B),
//                textAlign = TextAlign.Center,
//                lineHeight = 20.sp,
//                fontFamily = FontFamily(Font(R.font.inter))
//            )
//        }
//    }
//}

// ─────────────────────────────────────────────────────────────────────────────
// Contact card
// ─────────────────────────────────────────────────────────────────────────────

 */

