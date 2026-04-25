package com.app.cities.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.cities.domain.model.City

@Composable
fun CityListScreen(
    viewModel: CityListViewModel
) {
    val state by viewModel.uiState.collectAsState()

    CityListContent(
        state = state,
        onSearch = { query -> viewModel.onSearch(query) }
    )
}

@Composable
fun CityListContent(
    state: CityListUiState,
    onSearch: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TextField(
            value = state.query,
            onValueChange = { onSearch(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Search city...") },
            singleLine = true
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }

        state.error?.let {
            Text(
                text = it,
                modifier = Modifier.padding(16.dp)
            )
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = state.cities,
                key = { it.name + it.country }
            ) { city ->
                CityItem(city = city)
            }
        }
    }
}

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

@Preview(showBackground = true)
@Composable
fun CityListPreview() {
    val fakeState = CityListUiState(
        cities = listOf(
            City("Buenos Aires", "AR", -50.2, -68.4, "buenos aires"),
            City("Berlin", "DE", 92.63, 10.1, "berlin")
        ),
        isLoading = false,
        query = ""
    )

    CityListContent(
        state = fakeState,
        onSearch = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CityListLoadingPreview() {
    CityListContent(
        state = CityListUiState(isLoading = true),
        onSearch = {}
    )
}
