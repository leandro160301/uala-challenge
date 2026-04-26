package com.app.cities.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
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
import com.app.cities.presentation.common.ErrorView
import com.app.cities.presentation.common.LoadingView
import com.app.cities.presentation.list.components.CityList

@Composable
fun CityListScreen(
    viewModel: CityListViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    CityListContent(
        modifier = modifier,
        state = state,
        onSearch = { query -> viewModel.onSearch(query) },
        onFavoriteClick = { cityId -> viewModel.onToggleFavorite(cityId) },
        onToggleFavoritesFilter = { viewModel.onToggleFavoritesFilter() },
        onCityClick = { city -> viewModel.onCitySelected(city) },
        onDetailClick = { city -> viewModel.onCityDetailSelected(city) }
    )
}

@Composable
fun CityListContent(
    state: CityListUiState,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFavoriteClick: (Int) -> Unit,
    onToggleFavoritesFilter: () -> Unit,
    onCityClick: (City) -> Unit,
    onDetailClick: (City) -> Unit
) {

    Column(modifier = modifier.padding(8.dp)) {

        TextField(
            value = state.query,
            onValueChange = onSearch,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search city...") },
            singleLine = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Only favorites",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = state.showOnlyFavorites,
                onCheckedChange = { onToggleFavoritesFilter() }
            )
        }

        when {
            state.isLoading -> LoadingView()
            state.error != null -> ErrorView(message = state.error)
            else -> CityList(
                cities = state.cities,
                selectedCityId = state.selectedCityId,
                onFavoriteClick = onFavoriteClick,
                onCityClick = onCityClick,
                onDetailClick = onDetailClick
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun CityListPreview() {
    val fakeState = CityListUiState(
        cities = listOf(
            City(id = 1,"Buenos Aires", "AR", -50.2, -68.4, "buenos aires", false),
            City(id = 2,"Berlin", "DE", 92.63, 10.1, "berlin", true)
        ),
        isLoading = false,
        query = ""
    )

    CityListContent(
        state = fakeState,
        onSearch = {},
        onFavoriteClick = {},
        onToggleFavoritesFilter = {},
        onCityClick = {},
        onDetailClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CityListLoadingPreview() {
    CityListContent(
        state = CityListUiState(isLoading = true),
        onSearch = {},
        onFavoriteClick = {},
        onToggleFavoritesFilter = {},
        onCityClick = {},
        onDetailClick = {}
    )
}
