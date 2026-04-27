package com.app.cities.presentation.list

import com.app.cities.domain.model.City

data class FilterParams(
    val cities: List<City>,
    val favorites: Set<Int>,
    val query: String,
    val showOnlyFavorites: Boolean
)