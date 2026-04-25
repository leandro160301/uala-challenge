package com.app.cities.presentation.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.cities.domain.model.City

@Composable
fun CityItem(
    city: City,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Text(
            text = "${city.name}, ${city.country}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Lat: ${city.lat}, Lon: ${city.lon}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}