package com.app.cities.presentation.list.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.cities.domain.model.City

@Composable
fun CityList(
    cities: List<City>,
    favoriteIds: Set<Int>,
    selectedCityId: Int?,
    onFavoriteClick: (Int) -> Unit,
    onCityClick: (City) -> Unit,
    onDetailClick: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(
            items = cities,
            key = { it.id }
        ) { city ->
            CityItem(
                city = city,
                isSelected = city.id == selectedCityId,
                isFavorite = favoriteIds.contains(city.id),
                onFavoriteClick = { onFavoriteClick(city.id) },
                onCityClick = onCityClick,
                onDetailClick = onDetailClick
            )
        }
    }
}