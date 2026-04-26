package com.app.cities.presentation.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.cities.domain.model.City

@Composable
fun CityItem(
    city: City,
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onClick() }
        ) {
            Text("${city.name}, ${city.country}")
            Text("Lat: ${city.lat}, Lon: ${city.lon}")
        }

        Text(
            text = if (city.isFavorite) "★" else "☆",
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable { onFavoriteClick() }
        )
    }
}