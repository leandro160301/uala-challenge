package com.app.cities.domain.usecase

import com.app.cities.domain.repository.CityRepository
import jakarta.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke(cityId: Int) {
        repository.toggleFavorite(cityId)
    }
}