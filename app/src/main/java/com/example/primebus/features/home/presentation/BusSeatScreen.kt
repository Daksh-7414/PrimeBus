package com.example.primebus.features.home.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.primebus.R
import com.example.primebus.SeatUiState
import com.example.primebus.core.navigation.appnavigation.NavRoutes
import com.example.primebus.data.models.Bus
import com.example.primebus.data.models.SeatModel
import com.example.primebus.data.models.SeatStatus
import com.example.primebus.data.models.SeatUIModel
import com.example.primebus.data.models.TripRequest
import com.example.primebus.features.home.viewmodels.BookingViewModel
import com.example.primebus.features.home.viewmodels.SeatViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun BusSeatScreen(
    seatViewModel: SeatViewModel = hiltViewModel(),
    bookingViewModel: BookingViewModel,
    navController: NavController
) {
    val selectedBus by bookingViewModel.selectedBus.collectAsStateWithLifecycle()
    val tripRequest by bookingViewModel.tripRequest.collectAsStateWithLifecycle()
    val uiState by seatViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(selectedBus, tripRequest) {
        val bus = selectedBus ?: return@LaunchedEffect
        val dateStr = bookingViewModel.journeyDateStr
        if (dateStr.isEmpty()) return@LaunchedEffect
        seatViewModel.loadSeats(
            busId = bus.busId,
            busType = bus.type,
            journeyDateStr = dateStr,
            totalSeats = bus.totalSeats
        )
    }

    DisposableEffect(Unit) {
        onDispose { seatViewModel.releaseAllSelectedSeats() }
    }

    LaunchedEffect(uiState) {
        if (uiState is SeatUiState.Error || uiState is SeatUiState.NoInternet) {
            delay(3000)
            seatViewModel.clearError()
        }
    }

    BusSeatContent(
        uiState = uiState,
        selectedBus = selectedBus,
        tripRequest = tripRequest,
        onBackClick = {
            seatViewModel.releaseAllSelectedSeats()
            navController.popBackStack()
        },
        onSeatClick = {
            seatViewModel.onSeatClick(it)
        },
        onRetryClick = {
            selectedBus?.let { bus ->
                seatViewModel.loadSeats(
                    busId = bus.busId,
                    busType = bus.type,
                    journeyDateStr = bookingViewModel.journeyDateStr,
                    totalSeats = bus.totalSeats
                )
            }
        },
        onProceedClick = { seatUIModels ->
            val seats = seatUIModels
                .filter { it.status == SeatStatus.SELECTED }
                .map { it.seat }
            bookingViewModel.setSelectedSeats(seats)
            bookingViewModel.createPassengersFromSeats()
            navController.navigate(NavRoutes.Checkout.route)
        }
    )
}

@Composable
fun BusSeatContent(
    uiState: SeatUiState,
    selectedBus: Bus?,
    tripRequest: TripRequest?,
    onBackClick: () -> Unit,
    onSeatClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    onProceedClick: (List<SeatUIModel>) -> Unit
) {
    val formattedDate = tripRequest?.journeyDate?.let { millis ->
        SimpleDateFormat("EEE, dd MMM", Locale.ENGLISH)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date(millis))
    } ?: "Date not selected"

    if (selectedBus == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val bus = selectedBus

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF2F7))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 4.dp)
        )
        {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier.size(30.dp))
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    stringResource(R.string.select_seats),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00226B),
                    fontFamily = FontFamily(Font(R.font.inter))
                )
                Text(
                    "${bus.busName} | ${bus.type}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }
        }

        when (uiState) {

            is SeatUiState.Loading -> {
                AppLoadingScreen(text = "Loading seats...")
            }

            is SeatUiState.NoInternet -> {
                NoInternetCard()
            }

            is SeatUiState.Error -> {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp), Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            uiState.message, fontSize = 14.sp,
                            color = Color(0xFFB00020), fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = onRetryClick,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF3D3BC4))
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            is SeatUiState.Empty -> {
                NoSeatAvailableCard()
            }

            is SeatUiState.Success -> {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        Box(
                            Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                Modifier.padding(16.dp, 12.dp)
                            ) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        stringResource(R.string.route),
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        stringResource(R.string.schedule),
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "${bus.source} → ${bus.destination}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                    Text(
                                        "$formattedDate | ${bus.departureTime}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = FontFamily(Font(R.font.inter))
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Box(
                            Modifier
                                .padding(16.dp, 8.dp)
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(16.dp))
                        ) {
                            FlowRow(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                LegendItem(
                                    stringResource(R.string.available),
                                    Color.White,
                                    Color.Gray
                                )
                                LegendItem(
                                    stringResource(R.string.selected),
                                    Color(0xFF3D3BC4),
                                    Color.Transparent
                                )
                                LegendItem(
                                    stringResource(R.string.booked),
                                    Color(0xFFDDE3EB),
                                    Color.Transparent
                                )
                                LegendItem(
                                    "Locked",
                                    Color(0xFFFFD580),
                                    Color.Transparent
                                )
                            }
                        }
                    }
                    item {
                        Box(
                            Modifier
                                .padding(16.dp, 8.dp, 16.dp, 16.dp)
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(16.dp))
                        ) {
                            SeatMap(
                                seatUIModels = uiState.seatUIModels,
                                busType = bus.type,
                                onSeatClick = onSeatClick
                            )
                        }
                    }
                }
            }

            else -> Unit
        }

        if (uiState is SeatUiState.Success) {
            val selectedCount = uiState.seatUIModels.count { it.status == SeatStatus.SELECTED }
            val totalPrice    = selectedCount * bus.price

            Box(Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(topStart = 13.dp, topEnd = 13.dp))
            )
            {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        Modifier.weight(2f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "SELECTED",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Spacer(Modifier.width(10.dp))
                            Box(Modifier
                                .background(Color(0xFFD0D7E8), RoundedCornerShape(8.dp))
                                .size(70.dp, 32.dp),
                                Alignment.Center
                            ) {
                                Text(
                                    "$selectedCount Seats",
                                    fontSize = 14.sp,
                                    color = Color(0xFF012470),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            "₹${totalPrice.toInt()}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = { onProceedClick(uiState.seatUIModels) },
                        enabled = selectedCount > 0,
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF3D3BC4)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
                    ) {
                        Text(
                            "Proceed",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            painterResource(R.drawable.right_arrow),
                            "Proceed",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoSeatAvailableCardPreview() {
    NoSeatAvailableCard()
}

@Composable
fun NoSeatAvailableCard() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF2F7)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.noseats),
            contentDescription = "image",
            modifier = Modifier.size(345.dp)
        )
    }
}

@Composable
fun SeatMap(
    seatUIModels: List<SeatUIModel>,
    busType: String,
    onSeatClick: (String) -> Unit
) {
    val isSleeper  = busType.contains("Sleeper", ignoreCase = true) &&
            !busType.contains("Non", ignoreCase = true)
    val seatHeight = if (isSleeper) 90.dp else 70.dp
    val rows       = seatUIModels.groupBy { it.seat.row }

    Column(Modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.front),
            Modifier.padding(16.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )

        rows.keys.sorted().forEach { rowIndex ->
            val rowSeats   = rows[rowIndex]!!.sortedBy { it.seat.column }
            val leftSeats  = rowSeats.filter { it.seat.column < 2 }
            val rightSeats = rowSeats.filter { it.seat.column >= 2 }

            Row(Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                leftSeats.forEach { uiModel ->
                    SeatBox(uiModel.seat.seatNumber, seatHeight, uiModel.status) {
                        onSeatClick(uiModel.seat.seatNumber)
                    }
                    Spacer(Modifier.width(12.dp))
                }
                if (!isSleeper && rightSeats.isNotEmpty()) Spacer(Modifier.width(30.dp))
                rightSeats.forEach { uiModel ->
                    SeatBox(uiModel.seat.seatNumber, seatHeight, uiModel.status) {
                        onSeatClick(uiModel.seat.seatNumber)
                    }
                    Spacer(Modifier.width(12.dp))
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun SeatBox(seatNumber: String, height: Dp, status: SeatStatus, onClick: () -> Unit) {
    val bg = when (status) {
        SeatStatus.AVAILABLE -> Color.White
        SeatStatus.SELECTED -> Color(0xFF3D3BC4)
        SeatStatus.BOOKED -> Color(0xFFDDE3EB)
        SeatStatus.LOCKED -> Color(0xFFFFD580)
    }
    val tc = when (status) {
        SeatStatus.AVAILABLE -> Color.Gray
        SeatStatus.SELECTED -> Color.White
        SeatStatus.BOOKED -> Color.Gray
        SeatStatus.LOCKED -> Color(0xFF7A5800)
    }
    Box(
        modifier = Modifier
            .size(width = 60.dp, height = height)
            .clip(RoundedCornerShape(12.dp))
            .background(bg, RoundedCornerShape(12.dp))
            .then(
                if (status == SeatStatus.AVAILABLE)
                    Modifier.border(2.dp, Color.LightGray, RoundedCornerShape(12.dp)) else Modifier
            )
            .clickable(enabled = status == SeatStatus.AVAILABLE || status == SeatStatus.SELECTED) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            seatNumber,
            color = tc,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }

}

@Composable
fun LegendItem(label: String, color: Color, borderStroke: Color) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(Modifier
            .size(20.dp)
            .background(color, RoundedCornerShape(5.dp))
            .border(BorderStroke(2.dp, borderStroke), RoundedCornerShape(5.dp)))
        Text(
            label,
            fontSize = 12.sp,
            color = Color.DarkGray
        )
    }
    Spacer(Modifier.width(12.dp))
}

@Preview(showBackground = true)
@Composable
private fun BusSeatContentPreview() {
    val mockBus = Bus(
        busId = "bus_101",
        busName = "Rajasthan Express",
        type = "AC Sleeper",
        rating = 4.2,
        price = 350.0,
        departureTime = "08:00 AM",
        arrivalTime = "10:30 AM",
        duration = "2h 30m",
        totalSeats = 10,
        boardingPoint = "Ajmer Bus Stand",
        droppingPoint = "Jaipur Sindhi Camp",
        source = "Ajmer",
        destination = "Jaipur"
    )
    val mockSeats = listOf(
        SeatUIModel(SeatModel("L1","L1",0,0), SeatStatus.SELECTED),
        SeatUIModel(SeatModel("R1","R1",0,1), SeatStatus.BOOKED),
        SeatUIModel(SeatModel("L2","L2",1,0), SeatStatus.AVAILABLE),
        SeatUIModel(SeatModel("R2","R2",1,1), SeatStatus.LOCKED),
    )
    BusSeatContent(
        uiState = SeatUiState.Success(mockSeats),
        selectedBus = mockBus,
        tripRequest = TripRequest("Ajmer", "Jaipur", System.currentTimeMillis()),
        onBackClick = {}, onSeatClick = {}, onRetryClick = {}, onProceedClick = {}
    )
}