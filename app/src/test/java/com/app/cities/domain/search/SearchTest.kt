package com.app.cities.domain.search

import com.app.cities.domain.model.City
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchTest {

    private fun defaultCities(): List<City> = listOf(
        City(1, "Alabama", "US", 0.0, 0.0, "alabama", isFavorite = false),
        City(2, "Albuquerque", "US", 0.0, 0.0, "albuquerque", isFavorite = false),
        City(3, "Berlin", "DE", 0.0, 0.0, "berlin", isFavorite = false)
    )

    @Test
    fun `given prefix Al returns only cities starting with Al`() {
        val cities = defaultCities()

        val result = searchByPrefix(cities, "Al")

        assertEquals(2, result.size)
    }

    @Test
    fun `given empty query returns all cities`() {
        val cities = defaultCities()

        val result = searchByPrefix(cities, "")

        assertEquals(3, result.size)
    }

    @Test
    fun `search is case insensitive`() {
        val cities = defaultCities()

        val result = searchByPrefix(cities, "al")

        assertEquals(2, result.size)
    }

    @Test
    fun `returns empty list when no matches found`() {
        val cities = defaultCities()

        val result = searchByPrefix(cities, "Bu")

        assertEquals(0, result.size)
    }

    @Test
    fun `does not match substrings that are not prefix`() {
        val cities = listOf(
            City(1, "New York", "US", 0.0, 0.0, "new york", isFavorite = false),
            City(2, "York", "UK", 0.0, 0.0, "york", isFavorite = false)
        )

        val result = searchByPrefix(cities, "York")

        assertEquals(1, result.size)
        assertEquals("York", result[0].name)
    }

    @Test
    fun `matches cities ignoring accents`() {
        val cities = listOf(
            City(1, "São Paulo", "BR", 0.0, 0.0, "sao paulo", isFavorite = false)
        )

        val result = searchByPrefix(cities, "sao")

        assertEquals(1, result.size)
    }

    @Test
    fun `normalize converts text to lowercase`() {
        val result = normalize("Berlin")
        assertEquals("berlin", result)
    }

    @Test
    fun `normalize removes accents`() {
        val result = normalize("São Paulo")
        assertEquals("sao paulo", result)
    }

    @Test
    fun `normalize keeps simple text unchanged`() {
        val result = normalize("london")
        assertEquals("london", result)
    }

    @Test
    fun `normalize handles empty string`() {
        val result = normalize("")
        assertEquals("", result)
    }

}
