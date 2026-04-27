package com.app.cities.di

import android.content.Context
import com.app.cities.data.local.FavoritesLocalDataSource
import com.app.cities.data.remote.CityRemoteDataSource
import com.app.cities.data.repository.CityRepositoryImpl
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.GetFavoriteIdsUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase
import com.app.cities.domain.usecase.ToggleFavoriteUseCase

class AppContainer(context: Context) {

    private val remoteDataSource = CityRemoteDataSource()
    private val favoritesLocalDataSource = FavoritesLocalDataSource(context)

    private val repository = CityRepositoryImpl(
        remoteDataSource,
        favoritesLocalDataSource
    )

    val getCitiesUseCase = GetCitiesUseCase(repository)
    val searchCitiesUseCase = SearchCitiesUseCase()
    val toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
    val getFavoriteIdsUseCase = GetFavoriteIdsUseCase(repository)
}