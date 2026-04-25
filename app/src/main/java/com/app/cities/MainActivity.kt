package com.app.cities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.app.cities.data.remote.CityRemoteDataSource
import com.app.cities.data.repository.CityRepositoryImpl
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase
import com.app.cities.presentation.list.CityListScreen
import com.app.cities.presentation.list.CityListViewModelFactory
import com.app.cities.ui.theme.CitiesAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<com.app.cities.presentation.list.CityListViewModel> {
        CityListViewModelFactory(
            getCitiesUseCase = GetCitiesUseCase(
                CityRepositoryImpl(
                    CityRemoteDataSource()
                )
            ),
            searchCitiesUseCase = SearchCitiesUseCase()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CitiesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    CityListScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}