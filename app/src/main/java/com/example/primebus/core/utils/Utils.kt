package com.example.primebus.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

@Composable
inline fun <reified VM : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController)
: VM {
    val navGraphRoute = destination.parent?.route
        ?: error("No parent navigation graph found for ${destination.route}")

    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return hiltViewModel(parentEntry)
}