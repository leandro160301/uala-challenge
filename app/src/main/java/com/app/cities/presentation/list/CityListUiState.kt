package com.app.cities.presentation.list

import com.app.cities.domain.model.City

sealed interface SelectedPanel {
    data object None : SelectedPanel
    data class Map(val cityId: Int) : SelectedPanel
    data class Detail(val cityId: Int) : SelectedPanel
}

data class CityListUiState(
    val cities: List<City> = emptyList(),
    val isLoading: Boolean = false,
    val query: String = "",
    val error: String? = null,
    val showOnlyFavorites: Boolean = false,
    val selectedCityId: Int? = null,
    val selectedPanel: SelectedPanel = SelectedPanel.None,
    val hasFavorites: Boolean = false,
    val favoriteIds: Set<Int> = emptySet()
)