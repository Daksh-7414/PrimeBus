package com.example.primebus.data.models

data class Route(
    val routeId: String,
    val from: String = "Jaipur",
    val to: String = "Delhi",
    val distanceKm: Int
)