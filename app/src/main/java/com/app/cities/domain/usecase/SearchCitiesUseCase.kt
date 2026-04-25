package com.app.cities.domain.usecase

import com.app.cities.domain.model.City
import com.app.cities.domain.search.searchByPrefix

class SearchCitiesUseCase {

    operator fun invoke(
        cities: List<City>,
        query: String
    ): List<City> {
        return searchByPrefix(cities, query)
    }
}