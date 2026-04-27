package com.app.cities.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.cities.domain.model.City
import com.app.cities.presentation.common.EmptyView
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
    Column(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onSearch,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search city...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(onClick = { onSearch("") }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Clear search",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Only favorites",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = state.showOnlyFavorites,
                    onCheckedChange = { onToggleFavoritesFilter() }
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            when {
                state.isLoading -> LoadingView()
                state.error != null -> ErrorView(message = state.error)
                state.cities.isEmpty() -> {
                    val message = when {
                        state.showOnlyFavorites && !state.hasFavorites ->
                            "No favorites yet"
                        state.showOnlyFavorites && state.query.isNotEmpty() ->
                            "No favorite cities match your search"
                        else ->
                            "No cities found"
                    }
                    EmptyView(message = message)
                }
                else -> CityList(
                    cities = state.cities,
                    selectedCityId = state.selectedCityId,
                    onFavoriteClick = onFavoriteClick,
                    onCityClick = onCityClick,
                    onDetailClick = onDetailClick,
                    favoriteIds = state.favoriteIds
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityListPreview() {
    val fakeState = CityListUiState(
        cities = listOf(
            City(id = 1,"Buenos Aires", "AR", -50.2, -68.4, "buenos aires"),
            City(id = 2,"Berlin", "DE", 92.63, 10.1, "berlin")
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
