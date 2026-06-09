package com.example.primebus.features.home.presentation

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.example.primebus.BusUiState
import com.example.primebus.R
import com.example.primebus.core.navigation.appnavigation.NavRoutes
import com.example.primebus.features.home.viewmodels.BookingViewModel
import com.example.primebus.features.home.viewmodels.BusViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun BusScreen(
    viewModel: BusViewModel = hiltViewModel(),
    bookingViewModel: BookingViewModel,
    navController: NavController
) {
    val tripRequest by bookingViewModel.tripRequest.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Log.d("BusScreen", "tripRequest = $tripRequest")

    val from = tripRequest?.from ?: ""
    val to = tripRequest?.to   ?: ""

    val formattedDate = tripRequest?.journeyDate?.let { millis ->
        SimpleDateFormat("EEE, dd MMM", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(millis))
    } ?: "Date not selected"


    LaunchedEffect(tripRequest) {
        if (tripRequest != null) {
            viewModel.fetchBuses(bookingViewModel.journeyRouteId)
        }
    }

    Column {
        TripToolbar(
            from = from,
            to = to,
            dateString = formattedDate,
            onBackClick = { navController.popBackStack() }
        )
        when(val state = uiState) {

            BusUiState.Loading -> {
                AppLoadingScreen("Loading buses...")
            }

            BusUiState.Empty -> {
                NoBusAvailableCard()
            }

            BusUiState.NoInternet -> {
                NoInternetCard()
            }

            is BusUiState.Error -> {
                //ErrorScreen(state.message)
            }

            is BusUiState.Success -> {
                val buses = state.buses
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F8FE))
                ) {
                    items(
                        items = buses,
                        key = { it.busId }
                    ) { bus ->

                        BusListItem(
                            bus = bus,
                            onViewSeatsClick = { selectedBus ->
                                bookingViewModel.selectBus(selectedBus)
                                navController.navigate(
                                    NavRoutes.Seat.route
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppLoadingScreen(text: String) {
    Column (
        modifier = Modifier.fillMaxSize().background(Color(0xFFEFF2F7)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CircularProgressIndicator(color = Color(0xFF00236E))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}

@Composable
fun TripToolbar(
    from: String,
    to: String,
    dateString: String,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF00236E),
                modifier = Modifier.size(28.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = "$from to $to",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF00226B),
                fontFamily = FontFamily(Font(R.font.inter))
            )
            Text(
                text = dateString,
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(R.font.inter))
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF00236E)
            )
        }
    }
}

@Composable
fun NoBusAvailableCard() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF2F7)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.screen),
            contentDescription = "image"
        )
    }
}

@Composable
fun NoInternetCard() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF2F7)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.no_internet),
            contentDescription = "image"
        )
    }
}

@Preview
@Composable
private fun AppLoadingScreenPreview() {
    AppLoadingScreen("Loading buses....")
}

@Preview(showBackground = true)
@Composable
private fun NoBusAvailableCardPreview() {
    NoBusAvailableCard()
}

@Preview(showBackground = true)
@Composable
fun PreviewTripToolbar() {
    TripToolbar(
        from = "Jaipur",
        to = "Ajmer",
        dateString = "Mon, 26 May",
        onBackClick = {},
        onEditClick = {}
    )
}
