package com.example.primebus.features.home.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.primebus.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import androidx.compose.ui.platform.LocalLocale
import com.example.primebus.data.models.TripRequest
import com.example.primebus.ui.theme.gradientBrush
import java.util.TimeZone

val InterFont = FontFamily(
    Font(R.font.inter, FontWeight.Normal)
)

@Composable
fun HomeScreen(
    onSearchClick: (TripRequest) -> Unit ,
    onNotificationClick: () -> Unit
) {
    val cityList = listOf(
        "Jaipur", "Ajmer", "Kota"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8FE))
            .padding(20.dp)
    ) {
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Good Morning, Daksh 🛫",
                            fontSize = 20.sp,
                            fontFamily = InterFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Where are you travelling today?",
                            fontSize = 13.sp,
                            fontFamily = InterFont,
                            color = Color.Gray
                        )
                    }
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notification",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                BusSearchSection(
                    cityList = cityList,
                    onSearchClick = onSearchClick
                )
            }
        }
    }
}

@Composable
fun LocationInputField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    placeholder: String,
    iconRes: ImageVector,
    iconTint: Color,
    suggestions: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val filteredCities = suggestions.filter {
        it.contains(value.trim(), ignoreCase = true)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isError) 1.dp else 0.dp,
                color = if (isError) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFEEF4FC))
    ) {

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            imageVector = iconRes,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(22.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = it.isNotBlank()
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                color = Color.Black
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 14.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
    if (isError) {
        Text(
            text = "This field is required",
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .width(150.dp)
            .heightIn(max = 250.dp),
        properties = PopupProperties(
            focusable = false
        )
    ) {
        if (filteredCities.isEmpty()) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "No city found",
                        color = Color.Gray
                    )
                },
                onClick = {},
                enabled = false
            )
        } else {
            filteredCities.forEach { city ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = city,
                            fontFamily = InterFont
                        )
                    },
                    onClick = {
                        onValueChange(city)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun BusSearchSection(
    cityList: List<String>,
    onSearchClick: (TripRequest) -> Unit
) {
    var source by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    val context = LocalContext.current

    var showValidation by remember { mutableStateOf(false) }

    val dateFormatter = SimpleDateFormat("dd MMM, yyyy", LocalLocale.current.platformLocale)
    val displayDate = selectedDate?.let {
        dateFormatter.format(Date(it))
    } ?: "Select Date"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp,16.dp)
        ) {

            Text("FROM", fontSize = 11.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            LocationInputField(
                value = source,
                onValueChange = {
                    source = it
                },
                isError = showValidation && source.isBlank(),
                placeholder = "Current Location",
                iconRes = Icons.Outlined.LocationOn,
                iconTint = Color(0xFF6366F1),
                suggestions = cityList
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("TO", fontSize = 11.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            LocationInputField(
                value = destination,
                onValueChange = {
                    destination = it
                },
                isError = showValidation && destination.isBlank(),
                placeholder = "Destination City",
                iconRes = Icons.Outlined.NearMe,
                iconTint = Color(0xFF06B6D4),
                suggestions = cityList
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("DATE", fontSize = 11.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFEEF4FC))
                    .clickable { showDatePicker = true }
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = null,
                    tint = Color(0xFF898C91),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    displayDate,
                    color = Color.DarkGray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W500,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    showValidation = true
                    if (
                        source.isBlank() ||
                        destination.isBlank() ||
                        selectedDate == null
                    ) {
                        return@Button
                    }
                    if (
                        source.trim().equals(
                            destination.trim(),
                            ignoreCase = true
                        )
                    ) {
                        Toast.makeText(
                            context,
                            "Source and destination cannot be same",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    onSearchClick(
                        TripRequest(
                            from = source.trim(),
                            to = destination.trim(),
                            journeyDate = selectedDate
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(gradientBrush, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Search Buses",
                    fontFamily = InterFont,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerModal(
            selectedDate = selectedDate,
            onDateSelected = { dateInMillis ->
                selectedDate = dateInMillis
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    selectedDate: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val today = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate ?: today,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= today
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    HomeScreen(onSearchClick = { _ -> },{})
}