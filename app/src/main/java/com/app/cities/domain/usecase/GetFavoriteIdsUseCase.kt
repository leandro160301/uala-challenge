package com.app.cities.domain.usecase

import com.app.cities.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteIdsUseCase(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<Set<Int>> {
        return repository.getFavoriteIds()
    }
}