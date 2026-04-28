package com.app.cities

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.cities.presentation.detail.CityDetailScreen
import com.app.cities.presentation.list.CityListScreen
import com.app.cities.presentation.list.CityListViewModel
import com.app.cities.presentation.map.MapScreen
import com.app.cities.presentation.navigation.Screen

@Composable
fun CitiesNavGraph(viewModel: CityListViewModel) {

    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val portraitNavController = rememberNavController()
    val landscapeNavController = rememberNavController()

    val navController = if (isLandscape) {
        landscapeNavController
    } else {
        portraitNavController
    }

    if (isLandscape) {

        Row(modifier = Modifier.fillMaxSize()) {

            CityListScreen(
                viewModel = viewModel,
                modifier = Modifier.weight(1f),
                onCityClick = { city ->
                    navController.navigate(Screen.Map.createRoute(city.id))
                },
                onDetailClick = { city ->
                    navController.navigate(Screen.Detail.createRoute(city.id))
                }
            )

            NavHost(
                navController = navController,
                startDestination = "empty",
                modifier = Modifier.weight(1f)
            ) {

                composable("empty") {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Selecciona una ciudad")
                    }
                }

                composable(
                    route = Screen.Map.route,
                    arguments = listOf(navArgument("cityId") { type = NavType.IntType })
                ) { backStackEntry ->

                    val cityId = backStackEntry.arguments?.getInt("cityId")!!

                    MapScreen(
                        city = viewModel.getCityById(cityId),
                        onBack = null
                    )
                }

                composable(
                    route = Screen.Detail.route,
                    arguments = listOf(navArgument("cityId") { type = NavType.IntType })
                ) { backStackEntry ->

                    val cityId = backStackEntry.arguments?.getInt("cityId")!!

                    CityDetailScreen(
                        city = viewModel.getCityById(cityId),
                        onBack = null
                    )
                }
            }
        }

    } else {

        NavHost(
            navController = navController,
            startDestination = Screen.List.route
        ) {

            composable(Screen.List.route) {
                CityListScreen(
                    viewModel = viewModel,
                    onCityClick = { city ->
                        navController.navigate(Screen.Map.createRoute(city.id))
                    },
                    onDetailClick = { city ->
                        navController.navigate(Screen.Detail.createRoute(city.id))
                    }
                )
            }

            composable(
                route = Screen.Map.route,
                arguments = listOf(navArgument("cityId") { type = NavType.IntType })
            ) { backStackEntry ->

                val cityId = backStackEntry.arguments?.getInt("cityId")!!

                MapScreen(
                    city = viewModel.getCityById(cityId),
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("cityId") { type = NavType.IntType })
            ) { backStackEntry ->

                val cityId = backStackEntry.arguments?.getInt("cityId")!!

                CityDetailScreen(
                    city = viewModel.getCityById(cityId),
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}