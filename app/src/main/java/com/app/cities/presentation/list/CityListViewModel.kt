package com.app.cities.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.cities.domain.model.City
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.GetFavoriteIdsUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase
import com.app.cities.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityListViewModel(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase
) : ViewModel() {

    private var allCities: List<City> = emptyList()

    private var favoriteIds: Set<Int> = emptySet()

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.List)
    val screenState: StateFlow<ScreenState> = _screenState

    private val _uiState = MutableStateFlow(CityListUiState(isLoading = true))
    val uiState: StateFlow<CityListUiState> = _uiState

    init {
        loadCities()
    }

    fun loadCities() {
        viewModelScope.launch {
            combine(
                getCitiesUseCase(),
                getFavoriteIdsUseCase()
            ) { cities, favorites ->
                cities to favorites
            }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { (cities, favorites) ->
                    allCities = cities
                    favoriteIds = favorites

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        hasFavorites = favorites.isNotEmpty(),
                        favoriteIds = favorites
                    )

                    applyFilters()
                }
        }
    }

    fun onSearch(query: String) {
        _uiState.value = _uiState.value.copy(query = query)

        viewModelScope.launch {
            applyFilters()
        }
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
        viewModelScope.launch {
            applyFilters()
        }
    }

    private suspend fun applyFilters() {
        val currentState = _uiState.value

        val filtered = withContext(Dispatchers.Default) {
            var result = allCities

            if (currentState.query.isNotEmpty()) {
                result = searchCitiesUseCase(result, currentState.query)
            }

            if (currentState.showOnlyFavorites) {
                result = result.filter { favoriteIds.contains(it.id) }
            }

            result
        }

        _uiState.value = currentState.copy(
            cities = filtered
        )
    }

    fun onCitySelected(city: City) {
        _uiState.value = _uiState.value.copy(selectedCityId = city.id)
        _screenState.value = ScreenState.Map(city)
    }

    fun onCityDetailSelected(city: City) {
        _uiState.value = _uiState.value.copy(selectedCityId = city.id)
        _screenState.value = ScreenState.Detail(city)
    }

    fun onBack() {
        _screenState.value = ScreenState.List
        _uiState.value = _uiState.value.copy(selectedCityId = null)
    }

}