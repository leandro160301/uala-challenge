package com.app.cities.data.remote

import com.app.cities.data.model.CityDto
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.json.Json

class CityRemoteDataSource {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun fetchCities(): List<CityDto> {
        val rawJson = fetchCitiesJson()

        return json.decodeFromString(rawJson)
    }

    private fun fetchCitiesJson(): String {
        val url = URL("https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json")
        val connection = url.openConnection() as HttpURLConnection

        return try {
            connection.requestMethod = "GET"
            connection.connect()
            connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }
}