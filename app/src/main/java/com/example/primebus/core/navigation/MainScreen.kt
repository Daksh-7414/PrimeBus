//package com.example.primebus.core.navigation
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.primebus.core.navigation.appnavigation.NavRoutes
//import com.example.primebus.core.navigation.appnavigation.NavigationHost
//import com.example.primebus.core.navigation.bottomnavigation.BottomNavigationBar
//import com.example.primebus.features.auth.viewmodels.AuthViewModel
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun MainScreen(
//    rootNavController: NavHostController,
//    authViewModel: AuthViewModel,
//    onLogout: () -> Unit
//) {
//    // Create a separate NavController for all main app screens (home, profile, trips, booking, etc.)
//    val mainNavController = rememberNavController()
//
//    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//
//    val bottomBarRoutes = listOf(
//        NavRoutes.Home.route,
//        NavRoutes.Search.route,
//        NavRoutes.Profile.route,
//        NavRoutes.Trips.route
//    )
//
//    val showBottomBar = currentRoute in bottomBarRoutes
//
//    Scaffold(
//        bottomBar = {
//            BottomNavigationBar(
//                navController = mainNavController,   // Use the inner controller for tab navigation
//                visible = showBottomBar
//            )
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier.padding(paddingValues)
//        ) {
//            // Pass both controllers to NavigationHost
//            NavigationHost(
//                navController = mainNavController,
//                rootNavController = rootNavController,
//                authViewModel = authViewModel,
//                onLogout = onLogout
//            )
//        }
//    }
//}