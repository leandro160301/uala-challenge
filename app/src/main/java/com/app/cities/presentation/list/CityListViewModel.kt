package com.app.cities.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.cities.domain.model.City
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase
import com.app.cities.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private val _uiState = MutableStateFlow(CityListUiState(isLoading = true))
    val uiState: StateFlow<CityListUiState> = _uiState

    private var searchJob: Job? = null

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
                        cities = cities,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }

    fun onSearch(query: String) {
        _uiState.value = _uiState.value.copy(query = query)

        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.Default) {
            val result = searchCitiesUseCase(allCities, query)

            _uiState.value = _uiState.value.copy(
                cities = result
            )
        }
    }

    fun onToggleFavorite(cityId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(cityId)
        }
    }
}