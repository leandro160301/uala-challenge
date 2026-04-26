package com.app.cities.domain.usecase

import com.app.cities.domain.repository.CityRepository

class ToggleFavoriteUseCase(
    private val repository: CityRepository
) {
    suspend operator fun invoke(cityId: Int) {
        repository.toggleFavorite(cityId)
    }
}