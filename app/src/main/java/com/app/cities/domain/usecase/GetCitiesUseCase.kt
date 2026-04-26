package com.app.cities.domain.usecase

import com.app.cities.domain.model.City
import com.app.cities.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow

class GetCitiesUseCase(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<List<City>> {
        return repository.getCities()
    }
}