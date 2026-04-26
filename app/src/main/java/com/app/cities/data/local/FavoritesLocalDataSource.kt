package com.app.cities.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "favorites")

class FavoritesLocalDataSource(private val context: Context) {

    private val FAVORITES_KEY = stringSetPreferencesKey("favorite_ids")

    val favorites: Flow<Set<Int>> = context.dataStore.data
        .map { preferences ->
            preferences[FAVORITES_KEY]
                ?.map { it.toInt() }
                ?.toSet()
                ?: emptySet()
        }

    suspend fun toggleFavorite(cityId: Int) {
        context.dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY]
                ?.map { it.toInt() }
                ?.toSet()
                ?: emptySet()

            val updated = if (current.contains(cityId)) {
                current - cityId
            } else {
                current + cityId
            }

            preferences[FAVORITES_KEY] = updated.map { it.toString() }.toSet()
        }
    }
}