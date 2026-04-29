package com.app.cities.presentation.list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.cities.domain.model.City
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CityListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsLoadingIndicator() {
        val state = CityListUiState(isLoading = true)

        composeTestRule.setContent {
            CityListContent(
                state = state,
                onSearch = {},
                onFavoriteClick = {},
                onToggleFavoritesFilter = {},
                onCityClick = {},
                onDetailClick = {}
            )
        }

        composeTestRule.onNodeWithText("Cities").assertExists()
        composeTestRule.onNodeWithText("No cities found").assertDoesNotExist()
    }

    @Test
    fun emptyState_showsEmptyMessage() {
        val state = CityListUiState(isLoading = false, cities = emptyList())

        composeTestRule.setContent {
            CityListContent(
                state = state,
                onSearch = {},
                onFavoriteClick = {},
                onToggleFavoritesFilter = {},
                onCityClick = {},
                onDetailClick = {}
            )
        }

        composeTestRule.onNodeWithText("No cities found").assertExists()
    }

    @Test
    fun successState_showsCityList() {
        val cities = listOf(
            City(1, "Buenos Aires", "AR", -34.6, -58.3, "buenos aires"),
            City(2, "Cordoba", "AR", -31.4, -64.1, "cordoba")
        )
        val state = CityListUiState(isLoading = false, cities = cities)

        composeTestRule.setContent {
            CityListContent(
                state = state,
                onSearch = {},
                onFavoriteClick = {},
                onToggleFavoritesFilter = {},
                onCityClick = {},
                onDetailClick = {}
            )
        }

        composeTestRule.onNodeWithText("Buenos Aires, AR").assertExists()
        composeTestRule.onNodeWithText("Cordoba, AR").assertExists()
    }

    @Test
    fun searchInput_triggersOnSearchCallback() {
        var searchedQuery = ""
        val state = CityListUiState(isLoading = false, cities = emptyList())

        composeTestRule.setContent {
            CityListContent(
                state = state,
                onSearch = { searchedQuery = it },
                onFavoriteClick = {},
                onToggleFavoritesFilter = {},
                onCityClick = {},
                onDetailClick = {}
            )
        }

        composeTestRule.onNodeWithText("Search city...").performTextInput("Bue")
        assertEquals("Bue", searchedQuery)
    }

    @Test
    fun filterFavorites_triggersCallback() {
        var callbackTriggered = false
        val state = CityListUiState(isLoading = false, cities = emptyList())

        composeTestRule.setContent {
            CityListContent(
                state = state,
                onSearch = { },
                onFavoriteClick = {},
                onToggleFavoritesFilter = { callbackTriggered = true },
                onCityClick = {},
                onDetailClick = {}
            )
        }

        composeTestRule.onNodeWithText("Favorites Only").performClick()
        assertTrue(callbackTriggered)
    }
}
