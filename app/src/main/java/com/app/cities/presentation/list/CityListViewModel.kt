package com.app.cities.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.cities.domain.model.City
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.GetFavoriteIdsUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase
import com.app.cities.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")
    private val showFavoritesFlow = MutableStateFlow(false)

    private var allCities: List<City> = emptyList()
    private var currentFilteredCities: List<City> = emptyList()
    private var currentPage = 1
    private val pageSize = 50

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
                .mapLatest { params ->

                    allCities = params.cities

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

                    currentFilteredCities = filtered
                    currentPage = 1
                    
                    val pagedCities = filtered.take(pageSize)

                    _uiState.value.copy(
                        cities = pagedCities,
                        query = params.query,
                        showOnlyFavorites = params.showOnlyFavorites,
                        hasFavorites = params.favorites.isNotEmpty(),
                        favoriteIds = params.favorites,
                        isLoading = false,
                        error = null
                    )
                }
                .collect { newState ->
                    _uiState.value = newState
                }
        }
    }

    fun getCityById(id: Int): City? {
        return allCities.firstOrNull { it.id == id }
    }

    fun loadMore() {
        if (currentFilteredCities.size > currentPage * pageSize) {
            currentPage++
            val nextCities = currentFilteredCities.take(currentPage * pageSize)
            _uiState.value = _uiState.value.copy(cities = nextCities)
        }
    }


    fun onToggleFavorite(cityId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(cityId)
        }
    }

    fun onSearch(query: String) {
        queryFlow.value = query

        _uiState.value = _uiState.value.copy(
            query = query
        )
    }

    fun onToggleFavoritesFilter() {
        showFavoritesFlow.value = !showFavoritesFlow.value
    }

    fun onCitySelected(city: City) {
        _uiState.value = _uiState.value.copy(
            selectedCityId = city.id,
            selectedPanel = SelectedPanel.Map(city.id)
        )
    }

    fun onCityDetailSelected(city: City) {
        _uiState.value = _uiState.value.copy(
            selectedCityId = city.id,
            selectedPanel = SelectedPanel.Detail(city.id)
        )
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedCityId = null,
            selectedPanel = SelectedPanel.None
        )
    }

}