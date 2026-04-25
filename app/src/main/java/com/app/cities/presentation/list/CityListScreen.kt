package com.app.cities.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.cities.domain.model.City
import com.app.cities.presentation.common.ErrorView
import com.app.cities.presentation.common.LoadingView
import com.app.cities.presentation.list.components.CityList

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
    Column(modifier = Modifier.fillMaxSize()) {

        TextField(
            value = state.query,
            onValueChange = onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Search city...") },
            singleLine = true
        )

        when {
            state.isLoading -> LoadingView()
            state.error != null -> ErrorView(message = state.error)
            else -> CityList(cities = state.cities)
        }
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
