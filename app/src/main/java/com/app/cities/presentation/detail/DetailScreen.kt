package com.app.cities.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.cities.domain.model.City

@Composable
fun CityDetailScreen(
    city: City,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("${city.name}, ${city.country}")
        Text("Latitude: ${city.lat}")
        Text("Longitude: ${city.lon}")
        Text("Favorite: ${city.isFavorite}")

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}