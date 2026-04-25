package com.app.cities.domain.repository

import com.app.cities.domain.model.City

interface CityRepository {
    fun getCities(): List<City>
}