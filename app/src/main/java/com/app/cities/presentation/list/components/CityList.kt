package com.app.cities.presentation.list.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.cities.domain.model.City

@Composable
fun CityList(
    cities: List<City>,
    onFavoriteClick: (Int) -> Unit,
    onCityClick: (City) -> Unit,
    onDetailClick: (City) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            items = cities,
            key = { it.id }
        ) { city ->
            CityItem(
                city = city,
                onFavoriteClick = { onFavoriteClick(city.id) },
                onCityClick = onCityClick,
                onDetailClick = onDetailClick
            )
        }
    }
}