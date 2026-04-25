package com.app.cities.domain.search

import com.app.cities.domain.model.City

fun searchByPrefix(
    cities: List<City>,
    query: String
): List<City> {
    if (query.isEmpty()) return cities

    val normalizedQuery = normalize(query)

    val startIndex = lowerBound(cities, normalizedQuery)
    val result = mutableListOf<City>()

    var i = startIndex
    while (i < cities.size && cities[i].normalizedName.startsWith(normalizedQuery)) {
        result.add(cities[i])
        i++
    }

    return result
}

fun lowerBound(cities: List<City>, query: String): Int {
    var left = 0
    var right = cities.size

    while (left < right) {
        val mid = (left + right) / 2

        if (cities[mid].normalizedName < query) {
            left = mid + 1
        } else {
            right = mid
        }
    }

    return left
}
