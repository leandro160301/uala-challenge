package com.app.cities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.app.cities.presentation.detail.CityDetailScreen
import com.app.cities.presentation.list.CityListScreen
import com.app.cities.presentation.list.CityListViewModel
import com.app.cities.presentation.list.CityListViewModelFactory
import com.app.cities.presentation.list.ScreenState
import com.app.cities.presentation.map.MapScreen
import com.app.cities.ui.theme.CitiesAppTheme

class MainActivity : ComponentActivity() {

    private val container by lazy {
        (application as CitiesApp).container
    }

    private val viewModel by viewModels<CityListViewModel> {
        CityListViewModelFactory(
            getCitiesUseCase = container.getCitiesUseCase,
            searchCitiesUseCase = container.searchCitiesUseCase,
            toggleFavoriteUseCase = container.toggleFavoriteUseCase,
            getFavoriteIdsUseCase = container.getFavoriteIdsUseCase,
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

                    if (isLandscape) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {

                            CityListScreen(
                                viewModel = viewModel,
                                modifier = Modifier.weight(1f)
                            )

                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                when (screenState) {
                                    is ScreenState.Map -> {
                                        MapScreen(
                                            modifier = Modifier.fillMaxSize(),
                                            city = (screenState as ScreenState.Map).city,
                                            onBack = null
                                        )
                                    }
                                    is ScreenState.Detail -> {
                                        CityDetailScreen(
                                            modifier = Modifier.fillMaxSize(),
                                            city = (screenState as ScreenState.Detail).city,
                                            onBack = null
                                        )
                                    }

                                    else -> Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationCity,
                                            contentDescription = null,
                                            tint = Color.Gray,
                                            modifier = Modifier.size(64.dp)
                                        )
                                        Text("Selecciona una ciudad para ver los detalles o el mapa", color = Color.Gray)
                                    }
                                }
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