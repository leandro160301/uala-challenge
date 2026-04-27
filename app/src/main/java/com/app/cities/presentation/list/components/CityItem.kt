package com.app.cities.presentation.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.cities.domain.model.City

@Composable
fun CityItem(
    city: City,
    isSelected: Boolean,
    isFavorite: Boolean,
    onCityClick: (City) -> Unit,
    onFavoriteClick: () -> Unit,
    onDetailClick: (City) -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onCityClick(city) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${city.name}, ${city.country}",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Lat: ${city.lat}, Lon: ${city.lon}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite)
                        Icons.Default.Star
                    else
                        Icons.Default.StarBorder,
                    contentDescription = "Favorite"
                )
            }

            TextButton(onClick = { onDetailClick(city) }) {
                Text("Info")
            }
        }
    }
}