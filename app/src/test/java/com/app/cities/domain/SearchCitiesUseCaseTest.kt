package com.app.cities.domain

// *CAMBIO* tests unitarios reales para SearchCitiesUseCase y la lógica de búsqueda

import com.app.cities.domain.model.City
import com.app.cities.domain.search.lowerBound
import com.app.cities.domain.search.normalize
import com.app.cities.domain.search.searchByPrefix
import com.app.cities.domain.usecase.SearchCitiesUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchCitiesUseCaseTest {

    private lateinit var useCase: SearchCitiesUseCase
    private lateinit var sortedCities: List<City>

    @Before
    fun setUp() {
        useCase = SearchCitiesUseCase()

        // Lista ordenada por normalizedName (como la devuelve el repositorio)
        sortedCities = listOf(
            city(1, "Alabama", "US"),
            city(2, "Albuquerque", "US"),
            city(3, "Anaheim", "US"),
            city(4, "Arizona", "US"),
            city(5, "Sydney", "AU"),
        )
    }

    // ── SearchCitiesUseCase ─────────────────────────────────────────────────

    @Test
    fun `invoke returns all cities when query is empty`() {
        val result = useCase(sortedCities, "")
        assertEquals(sortedCities, result)
    }

    @Test
    fun `invoke returns cities matching prefix case-insensitively`() {
        val result = useCase(sortedCities, "Al")
        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Alabama" })
        assertTrue(result.any { it.name == "Albuquerque" })
    }

    @Test
    fun `invoke returns single exact match`() {
        val result = useCase(sortedCities, "Ana")
        assertEquals(1, result.size)
        assertEquals("Anaheim", result.first().name)
    }

    @Test
    fun `invoke returns empty list when no city matches prefix`() {
        val result = useCase(sortedCities, "Zzz")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke trims leading and trailing whitespace from query`() {
        val result = useCase(sortedCities, "  Al  ")
        assertEquals(2, result.size)
    }

    // ── normalize ──────────────────────────────────────────────────────────

    @Test
    fun `normalize converts to lowercase`() {
        assertEquals("hello", normalize("HELLO"))
    }

    @Test
    fun `normalize strips diacritics`() {
        assertEquals("buenos aires", normalize("Buenos Aires"))
        assertEquals("cordoba", normalize("Córdoba"))
        assertEquals("munchen", normalize("München"))
    }

    // ── lowerBound ─────────────────────────────────────────────────────────

    @Test
    fun `lowerBound returns 0 for prefix smaller than all entries`() {
        val index = lowerBound(sortedCities, "a")
        assertEquals(0, index)
    }

    @Test
    fun `lowerBound returns size for prefix larger than all entries`() {
        val index = lowerBound(sortedCities, "z")
        assertEquals(sortedCities.size, index)
    }

    @Test
    fun `lowerBound returns correct insertion point for mid-list prefix`() {
        // "sy" is >= "arizona" but < end → should point to Sydney
        val index = lowerBound(sortedCities, "sy")
        assertEquals(sortedCities.indexOfFirst { it.name == "Sydney" }, index)
    }

    // ── searchByPrefix ─────────────────────────────────────────────────────

    @Test
    fun `searchByPrefix returns correct subset for prefix`() {
        val result = searchByPrefix(sortedCities, "Ar")
        assertEquals(listOf(sortedCities[3]), result)
    }

    @Test
    fun `searchByPrefix on empty list returns empty`() {
        val result = searchByPrefix(emptyList(), "Al")
        assertTrue(result.isEmpty())
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private fun city(id: Int, name: String, country: String): City {
        return City(
            id = id,
            name = name,
            country = country,
            lat = 0.0,
            lon = 0.0,
            normalizedName = normalize(name)
        )
    }
}
