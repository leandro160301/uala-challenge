package com.app.cities.presentation.list

import com.app.cities.domain.model.City

sealed class ScreenState {
    object List : ScreenState()
    data class Map(val city: City) : ScreenState()
    data class Detail(val city: City) : ScreenState()
}