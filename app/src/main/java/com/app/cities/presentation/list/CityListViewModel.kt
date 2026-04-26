package com.app.cities.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.cities.domain.model.City
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase
import com.app.cities.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CityListViewModel(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private var allCities: List<City> = emptyList()

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.List)
    val screenState: StateFlow<ScreenState> = _screenState

    private val _uiState = MutableStateFlow(CityListUiState(isLoading = true))
    val uiState: StateFlow<CityListUiState> = _uiState

    init {
        loadCities()
    }

    fun loadCities() {
        viewModelScope.launch {
            getCitiesUseCase()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { cities ->
                    allCities = cities

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )

                    applyFilters()
                }
        }
    }

    fun onSearch(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        applyFilters()
    }

    fun onToggleFavorite(cityId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(cityId)
        }
    }

    fun onToggleFavoritesFilter() {
        _uiState.value = _uiState.value.copy(
            showOnlyFavorites = !_uiState.value.showOnlyFavorites
        )
        applyFilters()
    }

    private fun applyFilters() {
        val currentState = _uiState.value

        var filtered = allCities

        if (currentState.query.isNotEmpty()) {
            filtered = searchCitiesUseCase(filtered, currentState.query)
        }

        if (currentState.showOnlyFavorites) {
            filtered = filtered.filter { it.isFavorite }
        }

        _uiState.value = currentState.copy(cities = filtered)
    }

    fun onCitySelected(city: City) {
        _selectedCity.value = city
        _screenState.value = ScreenState.Map(city)
    }

    fun onCityDetailSelected(city: City) {
        _screenState.value = ScreenState.Detail(city)
    }

    fun onBack() {
        _screenState.value = ScreenState.List
    }

}