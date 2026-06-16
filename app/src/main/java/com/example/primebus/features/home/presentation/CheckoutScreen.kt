package com.example.primebus.features.home.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.primebus.MainActivity
import com.example.primebus.R
import com.example.primebus.core.navigation.appnavigation.NavGraph
import com.example.primebus.core.navigation.appnavigation.NavRoutes
import com.example.primebus.core.utils.Constants.convenienceFee
import com.example.primebus.core.utils.Constants.gst
import com.example.primebus.data.models.Bus
import com.example.primebus.data.models.Passenger
import com.example.primebus.data.models.SeatModel
import com.example.primebus.features.home.viewmodels.BookingViewModel
import com.example.primebus.features.payment.RazorpayManager
import com.example.primebus.ui.theme.gradientBrush

@Preview
@Composable
fun CheckoutScreenContentPreview() {
    CheckoutScreenContent(
        bus = Bus(
            busName = "Rajasthan Express",
            type = "AC Sleeper",
            rating = 4.2,
            price = 799.0,
            departureTime = "10:30 PM",
            arrivalTime = "07:00 AM",
            source = "Jaipur",
            destination = "Delhi",
            duration = "8h 30m",
            totalSeats = 40,
            boardingPoint = "Sindhi Camp, Jaipur",
            droppingPoint = "ISBT Kashmiri Gate, Delhi"
        ),
        seats = listOf(
            SeatModel(seatId = "1", seatNumber = "A1"),
            SeatModel(seatId = "2", seatNumber = "A2")
        ),
        passengers = listOf(
            Passenger(seatId = "1", seatNumber = "A1", name = "Rahul Singh", age = "25", gender = "Male"),
            Passenger(seatId = "2", seatNumber = "A2", name = "Priya Singh", age = "23", gender = "Female")
        ),
        contactPhone = "9876543210",
        contactEmail = "rahul@example.com",
        onBackClick = {},
        onPhoneChange = {},
        onEmailChange = {},
        onProceedClick = {},
        onUpdatePassenger = { _, _ -> },
        finalTotalRupees = 444.0,
        validationError = null
    )
}

@Composable
fun CheckoutScreen(
    bookingViewModel: BookingViewModel,
    onBackClick: () -> Unit,
    navController: NavController,
) {
    val bus by bookingViewModel.selectedBus.collectAsState()
    val seats by bookingViewModel.selectedSeats.collectAsState()
    val passengers by bookingViewModel.passengers.collectAsState()
    val contactPhone by bookingViewModel.contactPhone.collectAsState()
    val contactEmail by bookingViewModel.contactEmail.collectAsState()

    val baseFare = seats.size * (bus?.price ?: 0.0)

    val finalTotalRupees = baseFare + gst + convenienceFee
    val finalTotalPaise = (finalTotalRupees * 100).toInt()

    val context = LocalContext.current
    val activity = context as MainActivity

    var validationError by remember { mutableStateOf<String?>(null) }
    var paymentError by remember { mutableStateOf<String?>(null) }

    if (paymentError != null) {
        AlertDialog(
            onDismissRequest = { paymentError = null },
            title = { Text("Payment Failed", fontWeight = FontWeight.Bold) },
            text = { Text(paymentError ?: "", color = Color.Gray) },
            confirmButton = {
                Button(
                    onClick = { paymentError = null },
                    colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D3BC4))
                ) { Text("Try Again") }
            },
            dismissButton = {
                TextButton(onClick = { paymentError = null; onBackClick() }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    CheckoutScreenContent(
        bus = bus,
        seats = seats,
        passengers = passengers,
        contactPhone = contactPhone,
        contactEmail = contactEmail,
        finalTotalRupees = finalTotalRupees,
        validationError = validationError,
        onBackClick = onBackClick,
        onPhoneChange = bookingViewModel::updateContactPhone,
        onEmailChange = bookingViewModel::updateContactEmail,
        onUpdatePassenger = { seatId, updated ->
            bookingViewModel.updatePassenger(seatId, updated)
        },
        onProceedClick = {
            if (!bookingViewModel.isBookingValid()) {
                validationError = "Please fill in all passenger details (name, age, gender)"
                return@CheckoutScreenContent
            }
            validationError = null

            Log.d("Checkout", "Validation passed. Opening Razorpay: ₹$finalTotalRupees ($finalTotalPaise paise)")

            val razorpay = RazorpayManager()
            razorpay.openPayment(
                activity = activity,
                amount   = finalTotalPaise,
                onSuccess = { paymentId ->
                    Log.d("Checkout", "Payment SUCCESS: paymentId=$paymentId")
                    bookingViewModel.confirmBooking(
                        paymentId = paymentId,
                        onSuccess = { bookingId ->
                            Log.d("Checkout", "Booking saved: bookingId=$bookingId")
                            navController.navigate(NavRoutes.BookingSuccess.route)

                        },
                        onFailure = { error ->
                            Log.e("Checkout", "Booking save failed: $error | paymentId=$paymentId")
                            paymentError = "Payment done (ID: $paymentId) but booking save failed.\nContact support with this ID."
                        }
                    )
                },
                onError = { error ->
                    Log.e("Checkout", "Payment FAILED: $error")
                    paymentError = error
                }
            )
        }
    )
}

@Composable
fun CheckoutScreenContent(
    bus: Bus?,
    seats: List<SeatModel>,
    passengers: List<Passenger>,
    contactPhone: String,
    contactEmail: String,
    onBackClick: () -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onProceedClick: () -> Unit,
    onUpdatePassenger: (String, Passenger) -> Unit,
    finalTotalRupees: Double,
    validationError: String?
) {
    if (bus == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Booking Data Found")
        }
        return
    }

    val totalPrice = seats.size * bus.price

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Booking Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter))
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${bus.source} → ${bus.destination}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Text(
                            text = "₹${finalTotalRupees.toInt()}",
                            fontSize = 20.sp,
                            color = Color(0xFF07266B),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Text(
                            text = bus.busName,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Text(
                            "TOTAL FARE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(7.dp))
                    HorizontalDivider(thickness = 0.3.dp)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                bus.departureTime,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                bus.boardingPoint,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1f).padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                bus.duration,
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
                                bus.arrivalTime,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                bus.droppingPoint,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                fontFamily = FontFamily(Font(R.font.inter))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 0.3.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.EventSeat,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Seats: ${seats.joinToString { it.seatNumber }}",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        /*
                        Button(
                            onClick        = {},
                            shape          = RoundedCornerShape(12.dp),
                            border         = BorderStroke(1.dp, Color(0xFF3D3BC4)),
                            colors         = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEFF6FF),
                                contentColor   = Color(0xFF3D3BC4)
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.Visibility, contentDescription = null,
                                tint = Color(0xFF3D3BC4), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("View Seats", fontSize = 16.sp, color = Color(0xFF3D3BC4),
                                fontWeight = FontWeight.Bold, fontFamily = FontFamily(Font(R.font.inter)))
                        }
                        */
                    }
                }
            }

            ContactDetailsUI(
                phone = contactPhone,
                email = contactEmail,
                onPhoneChange = onPhoneChange,
                onEmailChange = onEmailChange
            )

            PassengerDetailsScreen(
                passengers = passengers,
                onUpdatePassenger = onUpdatePassenger
            )

            if (validationError != null) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxWidth()
                        .background(Color(0xFFFFEBEB), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = validationError,
                        color = Color(0xFFB00020),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color(0xFFEEF4FC))
                    .padding(20.dp)
            ) {
                Text(
                    "Fare Summary",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = FontFamily(Font(R.font.inter))
                )
                Spacer(modifier = Modifier.height(14.dp))

                FareSummaryRow(
                    "Base Fare",
                    "₹${totalPrice.toInt()}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                FareSummaryRow(
                    "GST & Taxes",
                    "₹24.00"
                )
                Spacer(modifier = Modifier.height(8.dp))
                FareSummaryRow(
                    "Convenience Fee",
                    "₹20.00"
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(thickness = 0.6.dp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Total Paid",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Text(
                        "₹${finalTotalRupees.toInt()}",
                        color = Color(0xFF3F51B5),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White,
                    RoundedCornerShape(topStart = 13.dp, topEnd = 13.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(2f)
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
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFD0D7E8),
                                RoundedCornerShape(8.dp))
                            .size(70.dp, 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${seats.size} Seats",
                            fontSize = 14.sp,
                            color = Color(0xFF012470),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    "₹${finalTotalRupees.toInt()}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = onProceedClick,
                modifier = Modifier.weight(1.5f).background(gradientBrush,RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Text(
                    "Confirm",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = R.drawable.right_arrow),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun FareSummaryRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray,
            fontSize = 15.sp,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        Text(
            value,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 15.sp,
            fontFamily = FontFamily(Font(R.font.poppins_bold))
        )
    }
}

@Composable
fun ContactDetailsUI(
    phone: String, email: String,
    onPhoneChange: (String) -> Unit, onEmailChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp, 16.dp, 8.dp)
    ) {
        Text(
            "Contact Details",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Phone Number",
            fontSize = 14.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(6.dp))
        ContactInputField(
            value = phone,
            onValueChange = onPhoneChange,
            icon = Icons.Default.Call,
            placeholder = "+91 9876543210",
            keyboardType = KeyboardType.Phone
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Email",
            fontSize = 14.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(6.dp))
        ContactInputField(
            value = email,
            onValueChange = onEmailChange,
            icon = Icons.Default.Email,
            placeholder = "traveler@example.com",
            keyboardType = KeyboardType.Email
        )
    }
}

@Composable
fun ContactInputField(
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    placeholder: String,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontSize = 15.sp
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F5F9),
            unfocusedContainerColor = Color(0xFFF1F5F9),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun PassengerDetailsScreen(
    passengers: List<Passenger>,
    onUpdatePassenger: (String, Passenger) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Passenger Details",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        Spacer(modifier = Modifier.height(14.dp))
        passengers.forEachIndexed { index, passenger ->
            PassengerCard(
                index = index,
                passenger = passenger,
                onUpdate = { updated ->
                    onUpdatePassenger(passenger.seatId, updated)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PassengerCard(
    index: Int,
    passenger: Passenger,
    onUpdate: (Passenger) -> Unit
) {
    Card(
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFF3D3BC4), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "${index + 1}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Passenger ${index + 1} — Seat ${passenger.seatNumber}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Column(modifier = Modifier.weight(0.6f)
                ) {
                    Text(
                        "Name",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PassengerInputField(
                        value = passenger.name,
                        onValueChange = { onUpdate(passenger.copy(name = it)) },
                        placeholder = "Full Name",
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words   // <-- explicit
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(0.4f)) {
                    Text("Age", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    PassengerInputField(
                        value = passenger.age,
                        onValueChange = {
                            onUpdate(passenger.copy(age = it))
                        },
                        keyboardType = KeyboardType.Number,
                        placeholder = "Age"
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Gender",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            GenderSelection(
                selected = passenger.gender,
                onSelect = {
                    onUpdate(passenger.copy(gender = it))
                }
            )
        }
    }
}

@Composable
fun PassengerInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, fontSize = 15.sp) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F5F9),
            unfocusedContainerColor = Color(0xFFF1F5F9),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = capitalization
        )
    )
}

@Composable
fun GenderSelection(
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("Male", "Female", "Other").forEach { gender ->
            val isSelected = gender == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) Color(0xFF3D3BC4) else Color(0xFFF1F5F9))
                    .clickable { onSelect(gender) }
                    .padding(horizontal = 16.dp, vertical = 9.dp)
            ) {
                Text(
                    gender,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color(0xFF696F73)
                )
            }
        }
    }
}