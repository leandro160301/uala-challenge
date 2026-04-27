package com.app.cities.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.cities.domain.model.City

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    city: City,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

        TopAppBar(
            title = {
                Text("${city.name}, ${city.country}")
            },
            navigationIcon = {
                onBack?.let {
                    IconButton(onClick = it) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Coordinates",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Latitude: ${city.lat}")
            Text("Longitude: ${city.lon}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityListLoadingPreview() {
    CityDetailScreen(
        city = City(id = 1,"Buenos Aires", "AR", -50.2, -68.4, "buenos aires"),
        onBack = {  },
    )
}
