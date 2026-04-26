package com.app.cities.data.mapper

import com.app.cities.data.model.CityDto
import com.app.cities.domain.model.City
import com.app.cities.domain.search.normalize

fun CityDto.toDomain(): City {
    return City(
        id = id,
        name = name,
        country = country,
        lat = coord.lat,
        lon = coord.lon,
        normalizedName = normalize(name),
        isFavorite = false
    )
}

