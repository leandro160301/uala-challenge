package com.app.cities.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.cities.domain.model.City
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CityListViewModel(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase
) : ViewModel() {

    private var allCities: List<City> = emptyList()

    private val _uiState = MutableStateFlow(CityListUiState(isLoading = true))
    val uiState: StateFlow<CityListUiState> = _uiState

    private var searchJob: Job? = null

    init {
        loadCities()
    }

    fun loadCities() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val cities = getCitiesUseCase()
                allCities = cities

                _uiState.value = _uiState.value.copy(
                    cities = cities,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
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
}