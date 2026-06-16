package com.example.primebus.core.navigation.appnavigation

import android.net.Uri
import com.example.primebus.data.models.TripRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class NavRoutes(val route: String) {

    // AUTH GRAPH
    data object Login : NavRoutes("login")
    data object OtpScreen : NavRoutes("otp_screen/{phone}") {
        fun createPhoneRoute(phone: String): String = "otp_screen/$phone"
    }

    // MAIN GRAPH
    data object Home : NavRoutes("home")
    data object Help : NavRoutes("help")
    data object Profile : NavRoutes("profile")
    data object Trips : NavRoutes("trips")

    // TRIPS GRAPH
    data object TicketCard : NavRoutes("ticket_screen")

    // BOOKING GRAPH
    data object Bus : NavRoutes("bus_screen/{tripRequest}") {
        fun createRoute(tripRequest: TripRequest): String {
            val json    = Json.encodeToString(tripRequest)
            val encoded = Uri.encode(json)
            return "bus_screen/$encoded"
        }
    }
    data object Seat : NavRoutes("seat_screen")
    data object Checkout : NavRoutes("checkout_screen")
    data object BookingSuccess : NavRoutes("booking_success")

    // PROFILE GRAPH
    data object MyTrips : NavRoutes("my_trips")
    data object Passengers : NavRoutes("passengers")
    data object Offers : NavRoutes("offers")
    data object EditProfile : NavRoutes("edit_profile")
    data object PaymentMethods : NavRoutes("payment_methods")
    data object Notifications : NavRoutes("notifications")
    data object HelpCenter : NavRoutes("help_center")
    data object CustomerSupport : NavRoutes("customer_support")
    data object RefundPolicy : NavRoutes("refund_policy")
    data object PrivacyPolicy : NavRoutes("privacy_policy")
    data object TermsConditions : NavRoutes("terms_conditions")
    data object AboutPrimeBus : NavRoutes("about_primebus")
}
