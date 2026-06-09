//package com.example.primebus.core.navigation.appnavigation
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.*
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.*
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.compose.*
//import com.example.primebus.core.navigation.SearchScreen
//import com.example.primebus.features.profile.presentation.AboutPrimeBusScreen
//import com.example.primebus.features.profile.presentation.ContactSupportScreen
//import com.example.primebus.features.profile.presentation.EditProfileScreen
//import com.example.primebus.features.profile.presentation.HelpCenterScreen
//import com.example.primebus.features.profile.presentation.OffersScreen
//import com.example.primebus.features.profile.presentation.PaymentMethodScreen
//import com.example.primebus.features.profile.presentation.PrivacyPolicyScreen
//import com.example.primebus.features.profile.presentation.RefundPolicyScreen
//import com.example.primebus.features.profile.presentation.SavedPassengersScreen
//import com.example.primebus.features.profile.presentation.TermsConditionsScreen
//import com.example.primebus.core.utils.sharedViewModel
//import com.example.primebus.features.booking.presentation.BookedTripsScreen
//import com.example.primebus.features.booking.presentation.BookingSuccessScreen
//import com.example.primebus.features.booking.presentation.BusTicketCard
//import com.example.primebus.features.booking.presentation.CheckoutScreen
//import com.example.primebus.features.home.presentation.BusScreen
//import com.example.primebus.features.home.presentation.BusSeatScreen
//import com.example.primebus.features.home.presentation.HomeScreen
//import com.example.primebus.features.auth.viewmodels.AuthViewModel
//import com.example.primebus.features.booking.viewmodels.BookedTripsViewModel
//import com.example.primebus.features.home.viewmodels.BookingViewModel
//import com.example.primebus.features.profile.presentation.PlaceholderScreen
//import com.example.primebus.features.profile.presentation.ProfileScreen
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun NavigationHost(
//    navController: NavHostController,
//    rootNavController: NavHostController,
//    authViewModel: AuthViewModel,
//    onLogout: () -> Unit
//) {
//    NavHost(
//        navController = navController,
//        startDestination = NavRoutes.Home.route
//    ) {
//        composable(NavRoutes.Home.route) {
//            HomeScreen(
//                onSearchClick = { from, to, _ ->
//                    navController.navigate(
//                        NavRoutes.Bus.createRoute(from, to)
//                    )
//                }
//            )
//        }
//
//        composable(NavRoutes.Search.route) {
//            SearchScreen()
//        }
//
//        navigation(
//            route = NavGraph.PROFILE,
//            startDestination = NavRoutes.Profile.route
//        )
//        {
//            composable(NavRoutes.Profile.route) {
//                ProfileScreen(
//                    navController = navController,   // inner controller for profile sub‑screens
//                    onLogOut = {
//                        authViewModel.logout()
//                        onLogout()
//                    }
//                )
//            }
//
//            composable(NavRoutes.Passengers.route) {
//                SavedPassengersScreen(navController = navController)
//            }
//
//            composable(NavRoutes.Offers.route) {
//                OffersScreen(navController = navController)
//            }
//
//            composable(NavRoutes.EditProfile.route) {
//                EditProfileScreen(navController = navController)
//            }
//
//            composable(NavRoutes.PaymentMethods.route) {
//                PaymentMethodScreen(navController = navController)
//            }
//
//            composable(NavRoutes.Notifications.route) {
//                PlaceholderScreen("Notifications Screen")
//            }
//
//            composable(NavRoutes.HelpCenter.route) {
//                HelpCenterScreen(navController = navController)
//            }
//
//            composable(NavRoutes.ContactSupport.route) {
//                ContactSupportScreen(navController = navController)
//            }
//
//            composable(NavRoutes.RefundPolicy.route) {
//                RefundPolicyScreen(navController = navController)
//            }
//
//            composable(NavRoutes.PrivacyPolicy.route) {
//                PrivacyPolicyScreen(navController = navController)
//            }
//
//            composable(NavRoutes.TermsConditions.route) {
//                TermsConditionsScreen(navController = navController)
//            }
//
//            composable(NavRoutes.AboutPrimeBus.route) {
//                AboutPrimeBusScreen(navController = navController)
//            }
//        }
//
//        navigation(
//            route = NavGraph.TRIPS,
//            startDestination = NavRoutes.Trips.route
//        ) {
//            composable(NavRoutes.Trips.route) { backStackEntry ->
//                val vm: BookedTripsViewModel =
//                    backStackEntry.sharedViewModel(navController)
//
//                BookedTripsScreen(
//                    navController = navController,
//                    viewModel = vm
//                )
//            }
//
//            composable(NavRoutes.TicketCard.route) { backStackEntry ->
//                val vm: BookedTripsViewModel =
//                    backStackEntry.sharedViewModel(navController)
//
//                BusTicketCard(
//                    viewModel = vm,
//                    navController = navController,
//                    onBackClick = {
//                        navController.popBackStack()
//                    }
//                )
//            }
//        }
//
//        navigation(
//            route = NavGraph.BOOKING,
//            startDestination = NavRoutes.Bus.route
//        ) {
//            composable(NavRoutes.Bus.route) { backStackEntry ->
//                val vm: BookingViewModel =
//                    backStackEntry.sharedViewModel(navController)
//
//                BusScreen(
//                    from = backStackEntry.arguments?.getString("from") ?: "",
//                    to = backStackEntry.arguments?.getString("to") ?: "",
//                    bookingViewModel = vm,
//                    navController = navController
//                )
//            }
//
//            composable(NavRoutes.Seat.route) { backStackEntry ->
//                val vm: BookingViewModel =
//                    backStackEntry.sharedViewModel(navController)
//
//                BusSeatScreen(
//                    bookingViewModel = vm,
//                    navController = navController
//                )
//            }
//
//            composable(NavRoutes.Checkout.route) { backStackEntry ->
//                val vm: BookingViewModel =
//                    backStackEntry.sharedViewModel(navController)
//
//                CheckoutScreen(
//                    bookingViewModel = vm,
//                    navController = navController,
//                    onBackClick = {
//                        navController.popBackStack()
//                    }
//                )
//            }
//
//            composable(NavRoutes.BookingSuccess.route) {
//                BookingSuccessScreen(navController)
//            }
//        }
//    }
//}