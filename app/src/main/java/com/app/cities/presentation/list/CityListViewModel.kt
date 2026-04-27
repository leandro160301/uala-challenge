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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityListViewModel(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")
    private val showFavoritesFlow = MutableStateFlow(false)

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
                getFavoriteIdsUseCase(),
                queryFlow,
                showFavoritesFlow
            ) { cities, favorites, query, showOnlyFavorites ->
                FilterParams(cities, favorites, query, showOnlyFavorites)
            }
                .collect { params ->

                    val filtered = withContext(Dispatchers.Default) {
                        var result = params.cities

                        if (params.query.isNotEmpty()) {
                            result = searchCitiesUseCase(result, params.query)
                        }

                        if (params.showOnlyFavorites) {
                            result = result.filter { params.favorites.contains(it.id) }
                        }

                        result
                    }

                    _uiState.value = CityListUiState(
                        cities = filtered,
                        query = params.query,
                        showOnlyFavorites = params.showOnlyFavorites,
                        isLoading = false,
                        error = null,
                        hasFavorites = params.favorites.isNotEmpty(),
                        favoriteIds = params.favorites
                    )
                }
        }
    }

    fun onToggleFavorite(cityId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(cityId)
        }
    }

    fun onSearch(query: String) {
        queryFlow.value = query
    }

    fun onToggleFavoritesFilter() {
        showFavoritesFlow.value = !showFavoritesFlow.value
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