package com.app.cities.presentation.list

import com.app.cities.domain.model.City

data class CityListUiState(
    val cities: List<City> = emptyList(),
    val isLoading: Boolean = false,
    val query: String = "",
    val error: String? = null,
    val showOnlyFavorites: Boolean = false
)