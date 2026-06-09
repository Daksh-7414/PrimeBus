package com.example.primebus.core.navigation.appnavigation

import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.primebus.core.navigation.SearchScreen
import com.example.primebus.core.navigation.bottomnavigation.BottomNavigationBar
import com.example.primebus.features.auth.presentation.GoogleAuthHelper
import com.example.primebus.features.auth.presentation.LoginScreen
import com.example.primebus.features.auth.presentation.OTPScreen
import com.example.primebus.features.auth.viewmodels.AuthViewModel
import com.example.primebus.features.booking.presentation.BookedTripsScreen
import com.example.primebus.features.booking.presentation.BookingSuccessScreen
import com.example.primebus.features.booking.presentation.BusTicketCard
import com.example.primebus.features.booking.presentation.CheckoutScreen
import com.example.primebus.features.booking.viewmodels.BookedTripsViewModel
import com.example.primebus.features.home.presentation.BusScreen
import com.example.primebus.features.home.presentation.BusSeatScreen
import com.example.primebus.features.home.presentation.HomeScreen
import com.example.primebus.features.home.viewmodels.BookingViewModel
import com.example.primebus.features.profile.presentation.*
import com.example.primebus.core.utils.sharedViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.example.primebus.data.models.TripRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavigation() {

    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()

    val startDestination =
        if (authViewModel.checkAuthStatus()) {
            NavGraph.MAIN
        } else {
            NavGraph.AUTH
        }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        NavRoutes.Home.route,
        NavRoutes.Search.route,
        NavRoutes.Profile.route,
        NavRoutes.Trips.route
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    val fixedDensity = remember { Resources.getSystem().displayMetrics.density }

    CompositionLocalProvider(
        LocalDensity provides Density(
            LocalDensity.current.density,
            fontScale = 1f
        )
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    visible = showBottomBar
                )
            }
        )
        { paddingValues ->

                NavHost(
                    modifier = Modifier.padding(paddingValues),
                    navController = navController,
                    startDestination = startDestination
                ) {

                    // ================= AUTH GRAPH =================

                    navigation(
                        route = NavGraph.AUTH,
                        startDestination = NavRoutes.Login.route
                    ) {

                        composable(NavRoutes.Login.route) {

                            val googleAuthHelper =
                                remember { GoogleAuthHelper() }

                            LoginScreen(

                                authViewModel = authViewModel,

                                googleAuthHelper = googleAuthHelper,

                                onLoginSuccess = {

                                    navController.navigate(NavGraph.MAIN) {

                                        popUpTo(NavGraph.AUTH) {
                                            inclusive = true
                                        }

                                        launchSingleTop = true
                                    }
                                },

                                onNavigateToOtp = { phone ->

                                    navController.navigate(
                                        NavRoutes.OtpScreen.createPhoneRoute(phone)
                                    )
                                }
                            )
                        }

                        composable(
                            route = NavRoutes.OtpScreen.route
                        ) { backStackEntry ->

                            val phone =
                                backStackEntry.arguments?.getString("phone") ?: ""

                            OTPScreen(
                                navController = navController,
                                phone = phone
                            )
                        }
                    }

                    // ================= MAIN GRAPH =================

                    navigation(
                        route = NavGraph.MAIN,
                        startDestination = NavRoutes.Home.route
                    ) {

                        composable(NavRoutes.Home.route) {

                            HomeScreen(

                                onSearchClick = { tripRequest ->
                                    navController.navigate(
                                        NavRoutes.Bus.createRoute(tripRequest)  // ✅ pass whole object
                                    )

                                }
                            )
                        }

                        composable(NavRoutes.Search.route) {

                            SearchScreen()
                        }

                        // ================= PROFILE =================

                        navigation(
                            route = NavGraph.PROFILE,
                            startDestination = NavRoutes.Profile.route
                        )
                        {

                            composable(NavRoutes.Profile.route) {

                                ProfileScreen(

                                    navController = navController,

                                    onLogOut = {

                                        authViewModel.logout()

                                        navController.navigate(NavGraph.AUTH) {

                                            popUpTo(NavGraph.MAIN) {
                                                inclusive = true
                                            }

                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }

                            composable(NavRoutes.Passengers.route) {
                                SavedPassengersScreen(navController)
                            }

                            composable(NavRoutes.Offers.route) {
                                OffersScreen(navController)
                            }

                            composable(NavRoutes.EditProfile.route) {
                                EditProfileScreen(navController)
                            }

                            composable(NavRoutes.PaymentMethods.route) {
                                PaymentMethodScreen(navController)
                            }

                            composable(NavRoutes.Notifications.route) {
                                PlaceholderScreen("Notifications")
                            }

                            composable(NavRoutes.HelpCenter.route) {
                                HelpCenterScreen(navController)
                            }

                            composable(NavRoutes.ContactSupport.route) {
                                ContactSupportScreen(navController)
                            }

                            composable(NavRoutes.RefundPolicy.route) {
                                RefundPolicyScreen(navController)
                            }

                            composable(NavRoutes.PrivacyPolicy.route) {
                                PrivacyPolicyScreen(navController)
                            }

                            composable(NavRoutes.TermsConditions.route) {
                                TermsConditionsScreen(navController)
                            }

                            composable(NavRoutes.AboutPrimeBus.route) {
                                AboutPrimeBusScreen(navController)
                            }
                        }

                        // ================= TRIPS =================

                        navigation(
                            route = NavGraph.TRIPS,
                            startDestination = NavRoutes.Trips.route
                        )
                        {

                            composable(NavRoutes.Trips.route) { backStackEntry ->

                                val vm: BookedTripsViewModel =
                                    backStackEntry.sharedViewModel(navController)

                                BookedTripsScreen(
                                    navController = navController,
                                    viewModel = vm
                                )
                            }

                            composable(NavRoutes.TicketCard.route) { backStackEntry ->

                                val vm: BookedTripsViewModel =
                                    backStackEntry.sharedViewModel(navController)

                                BusTicketCard(
                                    viewModel = vm,
                                    navController = navController,
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                        // ================= BOOKING =================

                        navigation(
                            route = NavGraph.BOOKING,
                            startDestination = NavRoutes.Bus.route
                        )
                        {

                            composable(NavRoutes.Bus.route) { backStackEntry ->
                                val vm: BookingViewModel = backStackEntry.sharedViewModel(navController)

                                val json = backStackEntry.arguments?.getString("tripRequest")

                                LaunchedEffect(json) {

                                    if (
                                        json != null &&
                                        vm.tripRequest.value == null
                                    ) {

                                        val decoded =
                                            Uri.decode(json)

                                        val request =
                                            Json.decodeFromString<TripRequest>(
                                                decoded
                                            )

                                        vm.setTripRequest(request)

                                        Log.d(
                                            "TripRequest",
                                            request.toString()
                                        )
                                    }
                                }

                                BusScreen(
                                    bookingViewModel = vm,
                                    navController = navController
                                )
                            }

                            composable(NavRoutes.Seat.route) { backStackEntry ->

                                val vm: BookingViewModel =
                                    backStackEntry.sharedViewModel(navController)

                                BusSeatScreen(
                                    bookingViewModel = vm,
                                    navController = navController
                                )
                            }

                            composable(NavRoutes.Checkout.route) { backStackEntry ->

                                val vm: BookingViewModel =
                                    backStackEntry.sharedViewModel(navController)

                                CheckoutScreen(
                                    bookingViewModel = vm,
                                    navController = navController,
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            }

                            composable(NavRoutes.BookingSuccess.route) {

                                BookingSuccessScreen(navController)
                            }
                        }
                    }
                }
            }
    }
}