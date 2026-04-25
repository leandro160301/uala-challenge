package com.app.cities.domain.model

data class City(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val normalizedName: String
)