package com.app.cities.domain.repository

import com.app.cities.domain.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun getCities(): Flow<List<City>>
    fun getFavoriteIds(): Flow<Set<Int>>
    suspend fun toggleFavorite(cityId: Int)
}