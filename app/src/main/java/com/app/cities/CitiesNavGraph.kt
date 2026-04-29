package com.app.cities

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
fun CitiesNavGraph(
    viewModel: CityListViewModel = hiltViewModel()
) {
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
                    if (city != null) {
                        MapScreen(
                            city = city,
                            onBack = null,
                        )
                    } else {
                        CityNotFoundScreen(onBack = null)
                    }
                }

                is SelectedPanel.Detail -> {
                    val city = viewModel.getCityById(panel.cityId)
                    if (city != null) {
                        CityDetailScreen(
                            city = city,
                            onBack = null,
                        )
                    } else {
                        CityNotFoundScreen(onBack = null)
                    }
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

            val city = viewModel.getCityById(cityId)
            if (city != null) {
                MapScreen(
                    city = city,
                    onBack = {
                        viewModel.clearSelection()
                        navController.popBackStack()
                    },
                )
            } else {
                CityNotFoundScreen(
                    onBack = {
                        viewModel.clearSelection()
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("cityId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getInt("cityId")
                ?: return@composable

            val city = viewModel.getCityById(cityId)
            if (city != null) {
                CityDetailScreen(
                    city = city,
                    onBack = {
                        viewModel.clearSelection()
                        navController.popBackStack()
                    },
                )
            } else {
                CityNotFoundScreen(
                    onBack = {
                        viewModel.clearSelection()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
private fun CityNotFoundScreen(onBack: (() -> Unit)? = null) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "City not found",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
            )
            if (onBack != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack) {
                    Text(text = "Go Back")
                }
            }
        }
    }
}