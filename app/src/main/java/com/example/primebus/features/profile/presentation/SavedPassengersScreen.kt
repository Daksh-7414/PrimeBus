package com.example.primebus.features.profile.presentation

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.primebus.R
import com.example.primebus.data.models.Passenger

@Preview
@Composable
private fun SavedPassengersScreenPreview() {
    val navController = rememberNavController()
    SavedPassengersScreen(
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPassengersScreen(navController: NavHostController) {

    var showBottomSheet by remember { mutableStateOf(false) }

    val passengers = remember {
        mutableStateListOf(
            Passenger("1", "L1", "Rahul", "22", "Male"),
            Passenger("2", "L2", "Aman", "24", "Male")
        )
    }

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF00236F),
            Color(0xFF5929C9)
        ),
        start = Offset(50f, 1000f),
        end = Offset(1000f, 500f)
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F3F9))
        ) {

            PrivacyToolBar(
                title = "Saved Passengers",
                onBackClick={navController.popBackStack()}
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Manage Travelers",
                            fontSize = 18.sp,
                            color = Color(0xFF012470),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Save passenger details for faster bookings and seamless checkout.",
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }

                item {
                    PassengeDetails(
                        passengers = passengers,
                        onDeletePassenger = { passenger ->
                            passengers.remove(passenger)
                        }
                    )
                }
            }

            Button(
                onClick = { showBottomSheet = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        brush = gradientBrush,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentPadding = PaddingValues(vertical = 18.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_icon),
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    "Add Passenger",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }
        }

        if (showBottomSheet) {
            AddPassengerBottomSheet(
                onDismiss = { showBottomSheet = false },
                onAddPassenger = { passenger ->
                    passengers.add(passenger)
                    showBottomSheet = false
                }
            )
        }
    }
}

@Composable
fun PassengeDetails(
    passengers: List<Passenger>,
    onDeletePassenger: (Passenger) -> Unit
) {
    Column{
        Text(
            text = "Saved Passengers",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        Spacer(modifier = Modifier.height(16.dp))
        passengers.forEachIndexed { index, passenger ->
            PassengeeCard(
                passenger = passenger,
                onDelete = {
                    onDeletePassenger(passenger)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview
@Composable
private fun AddPassengerBottomSheetPreview() {
    AddPassengerBottomSheet({},{})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPassengerBottomSheet(
    onDismiss: () -> Unit,
    onAddPassenger: (Passenger) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,  // 👈 start fully expanded
        confirmValueChange = { true }
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "Add Passenger",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter))
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Save passenger details for faster future bookings",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.inter))
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Full Name",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(R.font.inter))
            )

            Spacer(Modifier.height(10.dp))

            CustomInputField(
                value = name,
                onValueChange = { name = it },
                icon = Icons.Outlined.Person,
                placeholder = "Enter Name",
                keyboardType = KeyboardType.Text
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Age",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(R.font.inter))
            )

            Spacer(Modifier.height(10.dp))

            CustomInputField(
                value = age,
                onValueChange = { age = it },
                icon = Icons.Outlined.Cake,
                placeholder = "Enter Age",
                keyboardType = KeyboardType.Number
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Gender",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(R.font.inter))
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                RadioButton(
                    selected = gender == "Male",
                    onClick = { gender = "Male" }
                )
                Text("Male")

                Spacer(Modifier.width(8.dp))

                RadioButton(
                    selected = gender == "Female",
                    onClick = { gender = "Female" }
                )
                Text("Female")
                Spacer(Modifier.width(8.dp))

                RadioButton(
                    selected = gender == "Others",
                    onClick = { gender = "Others" }
                )
                Text("Others")
            }

            Spacer(Modifier.height(20.dp))
            val gradientBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF00236F),
                    Color(0xFF5929C9)
                ),
                start = Offset(50f, 1000f),
                end = Offset(1000f, 500f)
            )
            Button(
                onClick = {
                    val passenger = Passenger(
                        seatId = System.currentTimeMillis().toString(),
                        seatNumber = "L${(1..20).random()}",
                        name = name,
                        age = age,
                        gender = gender
                    )
                    onAddPassenger(passenger)
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = gradientBrush,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentPadding = PaddingValues(vertical = 18.dp)
            ) {
                Text(
                    "Add Passenger",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview
@Composable
private fun PassengeeCardPreview() {
    PassengeeCard(
        Passenger(
            "1",
            "L1",
            "Rahul",
            "22",
            "Male"),
        {}
    )
}

@Composable
fun PassengeeCard(
    passenger: Passenger,
    onDelete: () -> Unit
) {

    val initials = passenger.name
        .split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFFE3E9F1),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color(0xFF00236F),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = passenger.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
                Text(
                    text = "${passenger.gender}, ${passenger.age} Yrs",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }

            Row {
                IconButton(onClick = { /* edit later */ }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFF00236E),
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}