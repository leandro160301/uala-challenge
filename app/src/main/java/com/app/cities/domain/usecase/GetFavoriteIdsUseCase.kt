package com.app.cities.domain.usecase

import com.app.cities.domain.repository.CityRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetFavoriteIdsUseCase @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<Set<Int>> {
        return repository.getFavoriteIds()
    }
}