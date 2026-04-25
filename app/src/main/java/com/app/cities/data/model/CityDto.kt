package com.app.cities.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    val country: String,
    val name: String,
    @SerialName("_id")
    val id: Int,
    val coord: CoordDto
)

data class CoordDto(
    val lon: Double,
    val lat: Double
)