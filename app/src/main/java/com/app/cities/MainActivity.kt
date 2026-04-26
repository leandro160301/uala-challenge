package com.app.cities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.app.cities.data.local.FavoritesLocalDataSource
import com.app.cities.data.remote.CityRemoteDataSource
import com.app.cities.data.repository.CityRepositoryImpl
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase
import com.app.cities.domain.usecase.ToggleFavoriteUseCase
import com.app.cities.presentation.detail.CityDetailScreen
import com.app.cities.presentation.list.CityListScreen
import com.app.cities.presentation.list.CityListViewModel
import com.app.cities.presentation.list.CityListViewModelFactory
import com.app.cities.presentation.list.ScreenState
import com.app.cities.presentation.map.MapScreen
import com.app.cities.ui.theme.CitiesAppTheme

class MainActivity : ComponentActivity() {

    private val remoteDataSource by lazy { CityRemoteDataSource() }
    private val favoritesLocalDataSource by lazy { FavoritesLocalDataSource(applicationContext) }

    private val repository by lazy {
        CityRepositoryImpl(
            remoteDataSource,
            favoritesLocalDataSource
        )
    }

    private val viewModel by viewModels<CityListViewModel> {
        CityListViewModelFactory(
            getCitiesUseCase = GetCitiesUseCase(repository),
            searchCitiesUseCase = SearchCitiesUseCase(),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CitiesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->

                    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

                    val screenState by viewModel.screenState.collectAsState()
                    val selectedCity by viewModel.selectedCity.collectAsState()

                    if (isLandscape) {
                        Row(modifier = Modifier.padding(padding)) {

                            CityListScreen(
                                viewModel = viewModel,
                                modifier = Modifier.weight(1f)
                            )

                            selectedCity?.let { city ->
                                MapScreen(
                                    modifier = Modifier.weight(1f),
                                    city = city,
                                    onBack = {}
                                )
                            }
                        }
                    } else {
                        when (screenState) {
                            is ScreenState.List -> {
                                CityListScreen(
                                    viewModel,
                                    modifier = Modifier.padding(padding)
                                )
                            }
                            is ScreenState.Map -> {
                                MapScreen(
                                    modifier = Modifier.padding(padding),
                                    city = (screenState as ScreenState.Map).city,
                                    onBack = { viewModel.onBack() }
                                )
                            }
                            is ScreenState.Detail -> {
                                CityDetailScreen(
                                    modifier = Modifier.padding(padding),
                                    city = (screenState as ScreenState.Detail).city,
                                    onBack = { viewModel.onBack() }
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}