package com.app.cities.domain.model

object CityExtraInfo {

    fun hemisphere(lat: Double): String =
        if (lat >= 0) "Northern Hemisphere" else "Southern Hemisphere"

    fun formattedCoordinates(lat: Double, lon: Double): String =
        "%.4f°, %.4f°".format(lat, lon)

    fun wikipediaUrl(city: City): String =
        "https://en.wikipedia.org/wiki/${city.name.replace(" ", "_")}"
}