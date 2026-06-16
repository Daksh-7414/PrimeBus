package com.example.primebus.features.profile.presentation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.primebus.R
import com.example.primebus.features.profile.viewmodels.ProfileViewModel
import com.example.primebus.ui.theme.gradientBrush
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {

    EditProfileContent(
        name = "Daksh Singh",
        dob = "2004-08-12",
        gender = "Male",
        state = "Rajasthan",
        phone = "+91 9876543210",
        email = "daksh@gmail.com",
        onNameChange = {},
        onDobChange = {},
        onGenderChange = {},
        onStateChange = {},
        onPhoneChange = {},
        onSaveProfile = {},
        onBackClick = {}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    EditProfileContent(
        name = viewModel.name,
        dob = viewModel.dob,
        gender = viewModel.gender,
        state = viewModel.state,
        phone = viewModel.phone,
        email = viewModel.email,
        onNameChange = viewModel::updateName,
        onDobChange = viewModel::updateDob,
        onGenderChange = viewModel::updateGender,
        onStateChange = viewModel::updateState,
        onPhoneChange = viewModel::updatePhone,
        onSaveProfile = {
            viewModel.saveProfile(
                onSuccess = {
                    Toast.makeText(
                        context,
                        "Profile Saved",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                },
                onFailure = {
                    Toast.makeText(
                        context,
                        it,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        },
        onBackClick = {
            navController.popBackStack()
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditProfileContent(
    name: String,
    dob: String,
    gender: String,
    state: String,
    phone: String,
    email: String,
    onNameChange: (String) -> Unit,
    onDobChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onStateChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSaveProfile: () -> Unit,
    onBackClick: () -> Unit

) {

    var originalName by rememberSaveable { mutableStateOf("") }
    var originalDob by rememberSaveable { mutableStateOf("") }
    var originalGender by rememberSaveable { mutableStateOf("") }
    var originalState by rememberSaveable { mutableStateOf("") }
    var originalPhone by rememberSaveable { mutableStateOf("") }

    var initialLoaded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(name, dob, gender, state, phone, email) {
        if (!initialLoaded && email.isNotBlank()) {
            originalName = name
            originalDob = dob
            originalGender = gender
            originalState = state
            originalPhone = phone
            initialLoaded = true
        }
    }

    val isProfileChanged =
        name != originalName ||
                dob != originalDob ||
                gender != originalGender ||
                state != originalState ||
                phone != originalPhone

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F3F9))
    ) {
        PrivacyToolBar(
            title = "Edit Profile",
            onBackClick = onBackClick
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                PersonalDetailsCard(
                    name = name,
                    onNameChange = onNameChange,
                    dob = dob,
                    onDobChange = onDobChange,
                    gender = gender,
                    onGenderChange = onGenderChange
                )
            }

            item {
                ContactDetailsCard(
                    stateOfResidence = state,
                    onStateChange = onStateChange,
                    phoneNumber = phone,
                    onPhoneChange = onPhoneChange,
                    email = email,
                )
            }
        }
        Button(
            onClick = {
                if (isProfileChanged) {
                    onSaveProfile()
                }
            },
            enabled = isProfileChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    brush = gradientBrush,
                    shape = RoundedCornerShape(20.dp)
                ),

            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(vertical = 16.dp)

        ) {
            Text(
                text = "Save Changes",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    enabled: Boolean = true,
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        enabled = enabled,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = Color(0xFF64748B)
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = FontFamily(Font(R.font.inter)),
                color = Color(0xFF94A3B8),
                fontSize = 14.sp
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF8FAFC),
            unfocusedContainerColor = Color(0xFFF8FAFC),
            focusedBorderColor = Color(0xFFCBD5E1),
            unfocusedBorderColor = Color(0xFFE2E8F0),
            focusedLeadingIconColor = Color(0xFF3B82F6),
            unfocusedLeadingIconColor = Color(0xFF64748B)
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDetailsCard(
    name: String,
    onNameChange: (String) -> Unit,
    dob: String,
    onDobChange: (String) -> Unit,
    gender: String,
    onGenderChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Personal Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter)),
                color = Color(0xFF0F172A)
            )
            CustomInputField(
                value = name,
                onValueChange = onNameChange,
                icon = Icons.Outlined.PersonOutline,
                placeholder = "Full Name",
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            ) {
                CustomInputField(
                    value = dob,
                    onValueChange = {},
                    icon = Icons.Outlined.CalendarMonth,
                    placeholder = "YYYY-MM-DD",
                    readOnly = true,
                    enabled = false,
                )
            }

            var expanded by remember {
                mutableStateOf(false)
            }

            val genderOptions = listOf(
                "Male",
                "Female",
                "Other",
                "Prefer not to say"
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Face,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = Color(0xFF64748B)
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Select Gender",
                            fontFamily = FontFamily(Font(R.font.inter)),
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC),
                        focusedBorderColor = Color(0xFFCBD5E1),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    ),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option)
                            },
                            onClick = {
                                onGenderChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            val formatter = DateTimeFormatter
                                .ofPattern("yyyy-MM-dd")
                                .withLocale(Locale.getDefault())
                            val formattedDate = localDate.format(formatter)
                            onDobChange(formattedDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailsCard(
    stateOfResidence: String,
    onStateChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
    email: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Contact Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter)),
                color = Color(0xFF0F172A)
            )
            var stateExpanded by remember {
                mutableStateOf(false)
            }
            val states = listOf(
                "Andhra Pradesh",
                "Arunachal Pradesh",
                "Assam",
                "Bihar",
                "Chhattisgarh",
                "Goa",
                "Gujarat",
                "Haryana",
                "Himachal Pradesh",
                "Jharkhand",
                "Karnataka",
                "Kerala",
                "Madhya Pradesh",
                "Maharashtra",
                "Punjab",
                "Rajasthan",
                "Tamil Nadu",
                "Telangana",
                "Uttar Pradesh",
                "West Bengal"
            )
            ExposedDropdownMenuBox(
                expanded = stateExpanded,
                onExpandedChange = {
                    stateExpanded = !stateExpanded
                }
            ) {
                OutlinedTextField(
                    value = stateOfResidence,
                    onValueChange = {},
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = Color(0xFF64748B)
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Select State",
                            fontFamily = FontFamily(Font(R.font.inter)),
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC),
                        focusedBorderColor = Color(0xFFCBD5E1),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    ),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = stateExpanded
                        )
                    }
                )
                ExposedDropdownMenu(
                    expanded = stateExpanded,
                    onDismissRequest = {
                        stateExpanded = false
                    }
                ) {
                    states.forEach { state ->
                        DropdownMenuItem(
                            text = {
                                Text(state)
                            },
                            onClick = {
                                onStateChange(state)
                                stateExpanded = false
                            }
                        )
                    }
                }
            }
            CustomInputField(
                value = phoneNumber,
                onValueChange = onPhoneChange,
                icon = Icons.Outlined.Phone,
                placeholder = "+91 9876543210",
                keyboardType = KeyboardType.Phone,
            )

            CustomInputField(
                value = email,
                onValueChange = {},
                icon = Icons.Outlined.Email,
                placeholder = "name@example.com",
                keyboardType = KeyboardType.Email,
                readOnly = true,
                enabled = false
            )
        }
    }
}