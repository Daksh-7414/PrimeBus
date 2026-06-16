package com.example.primebus.core.navigation.bottomnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.HelpCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.primebus.core.navigation.appnavigation.NavRoutes

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {

    data object Home : BottomNavItem(
        route = NavRoutes.Home.route,
        title = "Home",
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Help : BottomNavItem(
        route = NavRoutes.Help.route,
        title = "Help",
        selectedIcon = Icons.Rounded.Forum,
        unselectedIcon = Icons.Outlined.Forum
    )

    data object Trips : BottomNavItem(
        route = NavRoutes.Trips.route,
        title = "Trips",
        selectedIcon = Icons.Rounded.ConfirmationNumber,
        unselectedIcon = Icons.Outlined.ConfirmationNumber
    )

    data object Profile : BottomNavItem(
        route = NavRoutes.Profile.route,
        title = "Profile",
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.Person
    )
}