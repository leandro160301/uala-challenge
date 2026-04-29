package com.app.cities.domain.usecase

import com.app.cities.domain.model.City
import com.app.cities.domain.repository.CityRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<List<City>> {
        return repository.getCities()
    }
}