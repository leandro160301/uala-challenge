package com.app.cities.data.repository

import com.app.cities.data.mapper.toDomain
import com.app.cities.data.remote.CityRemoteDataSource
import com.app.cities.domain.model.City
import com.app.cities.domain.repository.CityRepository

class CityRepositoryImpl(
    private val remoteDataSource: CityRemoteDataSource
) : CityRepository {

    private var cachedCities: List<City>? = null

    override fun getCities(): List<City> {
        if (cachedCities != null) return cachedCities!!

        val cities = remoteDataSource.fetchCities()
            .map { it.toDomain() }
            .sortedWith(compareBy({ it.normalizedName }, { it.country }))

        cachedCities = cities
        return cities
    }
}