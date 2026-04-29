package com.app.cities.di

import android.content.Context
import com.app.cities.data.local.FavoritesLocalDataSource
import com.app.cities.data.remote.CityRemoteDataSource
import com.app.cities.data.repository.CityRepositoryImpl
import com.app.cities.domain.repository.CityRepository
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.GetFavoriteIdsUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase
import com.app.cities.domain.usecase.ToggleFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCityRemoteDataSource(): CityRemoteDataSource {
        return CityRemoteDataSource()
    }

    @Provides
    @Singleton
    fun provideFavoritesLocalDataSource(
        @ApplicationContext context: Context
    ): FavoritesLocalDataSource {
        return FavoritesLocalDataSource(context)
    }

    @Provides
    @Singleton
    fun provideCityRepository(
        remoteDataSource: CityRemoteDataSource,
        favoritesLocalDataSource: FavoritesLocalDataSource
    ): CityRepository {
        return CityRepositoryImpl(
            remoteDataSource,
            favoritesLocalDataSource
        )
    }

    @Provides
    fun provideSearchCitiesUseCase(): SearchCitiesUseCase {
        return SearchCitiesUseCase()
    }

}