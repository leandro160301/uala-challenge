package com.app.cities.presentation.navigation

sealed class Screen(val route: String) {
    object List : Screen("list")
    object Map : Screen("map/{cityId}") {
        fun createRoute(cityId: Int) = "map/$cityId"
    }
    object Detail : Screen("detail/{cityId}") {
        fun createRoute(cityId: Int) = "detail/$cityId"
    }
}