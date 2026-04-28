package com.app.cities

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.app.cities.presentation.list.SelectedPanel
import com.app.cities.presentation.map.MapScreen
import com.app.cities.presentation.navigation.Screen

@Composable
fun CitiesNavGraph(viewModel: CityListViewModel) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val uiState by viewModel.uiState.collectAsState()

    if (isLandscape) {
        LandscapeLayout(
            viewModel = viewModel,
            selectedPanel = uiState.selectedPanel,
        )
    } else {
        PortraitLayout(viewModel = viewModel)
    }
}

@Composable
private fun LandscapeLayout(
    viewModel: CityListViewModel,
    selectedPanel: SelectedPanel,
) {
    Row(modifier = Modifier.fillMaxSize()) {

        CityListScreen(
            viewModel = viewModel,
            modifier = Modifier.weight(1f),
            onCityClick = { city ->
                viewModel.onCitySelected(city)
            },
            onDetailClick = { city ->
                viewModel.onCityDetailSelected(city)
            },
        )

        AnimatedContent(
            targetState = selectedPanel,
            modifier = Modifier.weight(1f),
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "landscape_panel",
        ) { panel ->
            when (panel) {
                is SelectedPanel.None -> LandscapePlaceholder()

                is SelectedPanel.Map -> {
                    val city = viewModel.getCityById(panel.cityId)
                    MapScreen(
                        city = city,
                        onBack = null,
                    )
                }

                is SelectedPanel.Detail -> {
                    val city = viewModel.getCityById(panel.cityId)
                    CityDetailScreen(
                        city = city,
                        onBack = null,
                    )
                }
            }
        }
    }
}

@Composable
private fun LandscapePlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Select a city to get started",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun PortraitLayout(viewModel: CityListViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.List.route,
    ) {

        composable(Screen.List.route) {
            CityListScreen(
                viewModel = viewModel,
                onCityClick = { city ->
                    viewModel.onCitySelected(city)
                    navController.navigate(Screen.Map.createRoute(city.id))
                },
                onDetailClick = { city ->
                    viewModel.onCityDetailSelected(city)
                    navController.navigate(Screen.Detail.createRoute(city.id))
                },
            )
        }

        composable(
            route = Screen.Map.route,
            arguments = listOf(navArgument("cityId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getInt("cityId")
                ?: return@composable

            MapScreen(
                city = viewModel.getCityById(cityId),
                onBack = {
                    viewModel.clearSelection()
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("cityId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getInt("cityId")
                ?: return@composable

            CityDetailScreen(
                city = viewModel.getCityById(cityId),
                onBack = {
                    viewModel.clearSelection()
                    navController.popBackStack()
                },
            )
        }
    }
}