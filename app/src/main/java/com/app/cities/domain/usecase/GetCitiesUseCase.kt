package com.app.cities.domain.usecase

import com.app.cities.domain.model.City
import com.app.cities.domain.repository.CityRepository

class GetCitiesUseCase(
    private val repository: CityRepository
) {
    operator fun invoke(): List<City> {
        return repository.getCities()
    }
}