package com.app.cities.data.repository

import com.app.cities.data.local.FavoritesLocalDataSource
import com.app.cities.data.mapper.toDomain
import com.app.cities.data.remote.CityRemoteDataSource
import com.app.cities.domain.model.City
import com.app.cities.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CityRepositoryImpl(
    private val remoteDataSource: CityRemoteDataSource,
    private val favoritesLocalDataSource: FavoritesLocalDataSource
) : CityRepository {

    private var cachedCities: List<City>? = null

    override fun getCities(): Flow<List<City>> {
        val cities = cachedCities ?: remoteDataSource.fetchCities()
            .map { it.toDomain() }
            .sortedWith(compareBy({ it.normalizedName }, { it.country }))
            .also { cachedCities = it }

        return favoritesLocalDataSource.favorites.map { favoriteIds ->
            cities.map { city ->
                city.copy(isFavorite = favoriteIds.contains(city.id))
            }
        }
    }
}