package com.example.primebus.features.room

import androidx.room.TypeConverter
import com.example.primebus.data.models.Passenger
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BookingTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromSeatsList(seats: List<String>): String {
        return gson.toJson(seats)
    }

    @TypeConverter
    fun toSeatsList(seatsString: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(
            seatsString,
            type
        )
    }

    @TypeConverter
    fun fromPassengerList(passengers: List<Passenger>): String {
        return gson.toJson(passengers)
    }

    @TypeConverter
    fun toPassengerList(passengersString: String): List<Passenger> {
        val type = object : TypeToken<List<Passenger>>() {}.type
        return gson.fromJson(
            passengersString,
            type
        )
    }
}