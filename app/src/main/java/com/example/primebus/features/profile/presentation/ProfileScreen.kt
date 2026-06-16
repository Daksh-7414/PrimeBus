package com.example.primebus.features.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.primebus.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.primebus.core.navigation.appnavigation.NavGraph
import com.example.primebus.core.navigation.appnavigation.NavRoutes

data class MenuItemData(
    val title: String,
    val icon: Int? = null,
    val destination: NavRoutes
)

data class ProfileSection(
    val title: String,
    val items: List<MenuItemData>
)

@Composable
fun ProfileScreen(navController: NavHostController,onLogOut: () -> Unit) {

    val sections = remember {
        listOf(
            ProfileSection(
                "ACCOUNT SETTINGS",
                listOf(
                    MenuItemData("Edit Profile", R.drawable.edit_profile_icon, NavRoutes.EditProfile),
                    MenuItemData("Payment Methods", R.drawable.card_icon, NavRoutes.PaymentMethods),
                    MenuItemData("Notifications", R.drawable.notification, NavRoutes.Notifications)
                )
            ),
            ProfileSection(
                "SUPPORT",
                listOf(
                    MenuItemData("Help Center", R.drawable.help_center_icon,NavRoutes.Help),
                    MenuItemData("Customer Support", R.drawable.contact_support_icon, NavRoutes.CustomerSupport),
                    MenuItemData("Refund Policy", R.drawable.refund_policy_icon, NavRoutes.RefundPolicy)
                )
            ),
            ProfileSection(
                "ABOUT PRIMEBUS",
                listOf(
                    MenuItemData("Privacy Policy", destination = NavRoutes.PrivacyPolicy),
                    MenuItemData("Terms & Conditions",destination = NavRoutes.TermsConditions),
                    MenuItemData("About PrimeBus",destination = NavRoutes.AboutPrimeBus)
                )
            )
        )
    }
    Column() {
        ProfileToolBar(onBackClick = {
            navController.navigate(NavRoutes.Notifications.route)
        })
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F8FE))
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
                {
                    QuickActionItem(
                        label = "My Trips",
                        icon = R.drawable.bus_icon,
                        iconColor = Color(0xFF334F90),
                        iconBackgroundColor = Color(0xFFDDE4FF),
                        onClick = {
                            navController.navigate(NavGraph.TRIPS)
                        }
                    )

                    QuickActionItem(
                        label = "Passengers",
                        icon = R.drawable.passenger_icon,
                        iconColor = Color(0xFF722BE2),
                        iconBackgroundColor = Color(0xFFE9D8FD),
                        onClick = {
                            navController.navigate(NavRoutes.Passengers.route)
                        }
                    )

                    QuickActionItem(
                        label = "Offers",
                        icon = R.drawable.offers,
                        iconColor = Color(0xFF08976B),
                        iconBackgroundColor = Color(0xFFD1FAE5),
                        onClick = {
                            navController.navigate(NavRoutes.Offers.route)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(sections) { section ->
                ProfileSectionCard(
                    sectionTitle = section.title,
                    items = section.items,
                    onItemClick = { item ->
                        if (item.destination == NavRoutes.Help) {
                            navController.navigate(NavRoutes.Help.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(item.destination.route)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Button(
                    onClick = { onLogOut() },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.1.dp, Color(0xFFFFC7C7)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF8FBFF)
                    ),
                    contentPadding = PaddingValues(vertical = 20.dp)
                )
                {
                    Icon(
                        imageVector = Icons.Rounded.Logout,
                        contentDescription = "Logout",
                        tint = Color(0xFFD61F1F),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Logout from PrimeBus",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD61F1F),
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
                Text(
                    text = "APP VERSION 4.2.0 (PREMIUM BUILD)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 20.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFB6BDC8),
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}

@Composable
fun ProfileSectionCard(
    sectionTitle: String,
    items: List<MenuItemData>,
    onItemClick: (MenuItemData) -> Unit
) {
    Column {
        Text(
            text = sectionTitle,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.W600,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            items.forEachIndexed { index, item ->
                ProfileMenuItem(
                    title = item.title,
                    icon = item.icon,
                    onClick = { onItemClick(item) }
                )
                if (index != items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 18.dp),
                        thickness = 1.dp,
                        color = Color(0xFFF1F5F9)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    title: String,
    icon: Int? = null,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = title,
                    tint = Color(0xFF4B5563),
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = Color(0xFFD1D5DB),
                modifier = Modifier.size(24.dp)
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileToolBar(
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp, 10.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "My Profile",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.notification),
                contentDescription = "Back",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview
@Composable
private fun hello() {
    QuickActionItem(label = "Offers",
        icon = R.drawable.offers,
        iconColor = Color(0xFF08976B),
        iconBackgroundColor = Color(0xFFD1FAE5),
        onClick = {
        }
    )
}

@Composable
fun QuickActionItem(
    label: String,
    icon: Int,
    iconColor: Color,
    iconBackgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = iconBackgroundColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    val navController = rememberNavController()

    ProfileScreen(
        navController = navController,
        onLogOut = {}
    )
}