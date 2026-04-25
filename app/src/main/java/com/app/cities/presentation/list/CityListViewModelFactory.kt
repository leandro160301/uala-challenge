package com.app.cities.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase

class CityListViewModelFactory(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityListViewModel::class.java)) {
            return CityListViewModel(
                getCitiesUseCase,
                searchCitiesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}