package com.app.cities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.app.cities.presentation.list.CityListViewModel
import com.app.cities.presentation.list.CityListViewModelFactory
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
                CitiesNavGraph(viewModel = viewModel)
            }
        }
    }
}