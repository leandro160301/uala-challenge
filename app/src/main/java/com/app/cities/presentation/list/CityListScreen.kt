package com.app.cities.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    modifier: Modifier = Modifier,
    onCityClick: (City) -> Unit,
    onDetailClick: (City) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    CityListContent(
        modifier = modifier,
        state = state,
        onSearch = { query -> viewModel.onSearch(query) },
        onFavoriteClick = { cityId -> viewModel.onToggleFavorite(cityId) },
        onToggleFavoritesFilter = { viewModel.onToggleFavoritesFilter() },
        onCityClick = onCityClick,
        onDetailClick = onDetailClick,
        onLoadMore = { viewModel.loadMore() }
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
    onDetailClick: (City) -> Unit,
    onLoadMore: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Text(
                text = "Cities",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = state.query,
                onValueChange = onSearch,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search city...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(onClick = { onSearch("") }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            FilterChip(
                selected = state.showOnlyFavorites,
                onClick = onToggleFavoritesFilter,
                label = {
                    Text(
                        text = "Favorites Only",
                        fontWeight = FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> LoadingView()
                state.error != null -> ErrorView(message = state.error)
                state.cities.isEmpty() -> {
                    val message = when {
                        state.showOnlyFavorites && !state.hasFavorites -> "No favorites yet"
                        state.showOnlyFavorites && state.query.isNotEmpty() -> "No favorite cities match your search"
                        else -> "No cities found"
                    }
                    EmptyView(message = message)
                }
                else -> CityList(
                    cities = state.cities,
                    selectedCityId = state.selectedCityId,
                    onFavoriteClick = onFavoriteClick,
                    onCityClick = onCityClick,
                    onDetailClick = onDetailClick,
                    onLoadMore = onLoadMore,
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
