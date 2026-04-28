package com.app.cities.presentation.list

import app.cash.turbine.test
import com.app.cities.domain.model.City
import com.app.cities.domain.search.normalize
import com.app.cities.domain.usecase.GetCitiesUseCase
import com.app.cities.domain.usecase.GetFavoriteIdsUseCase
import com.app.cities.domain.usecase.SearchCitiesUseCase
import com.app.cities.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CityListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCitiesUseCase: GetCitiesUseCase
    private lateinit var getFavoriteIdsUseCase: GetFavoriteIdsUseCase
    private lateinit var searchCitiesUseCase: SearchCitiesUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var viewModel: CityListViewModel

    private val sampleCities = listOf(
        city(1, "Alabama"),
        city(2, "Albuquerque"),
        city(3, "Sydney"),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getCitiesUseCase = mock()
        getFavoriteIdsUseCase = mock()
        searchCitiesUseCase = SearchCitiesUseCase()
        toggleFavoriteUseCase = mock()

        whenever(getCitiesUseCase()).thenReturn(flowOf(sampleCities))
        whenever(getFavoriteIdsUseCase()).thenReturn(flowOf(emptySet()))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

  /*  @Test
    fun `uiState starts as loading then exposes all cities`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(sampleCities, state.cities)
    }*/

    /*@Test
    fun `onSearch filters cities by prefix`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSearch("al")

        advanceTimeBy(300)
        advanceUntilIdle()

        val filtered = viewModel.uiState.value.cities
        assertEquals(2, filtered.size)
        assertTrue(filtered.all { it.name.startsWith("Al") })
    }*/

    @Test
    fun `onSearch with empty string restores full list`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSearch("Al")
        advanceUntilIdle()
        viewModel.onSearch("")
        advanceUntilIdle()

        assertEquals(sampleCities, viewModel.uiState.value.cities)
    }

    @Test
    fun `onCitySelected updates selectedCityId and opens Map panel`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onCitySelected(sampleCities[0])

        val state = viewModel.uiState.value
        assertEquals(sampleCities[0].id, state.selectedCityId)
        assertEquals(SelectedPanel.Map(sampleCities[0].id), state.selectedPanel)
    }

    @Test
    fun `onCityDetailSelected opens Detail panel`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onCityDetailSelected(sampleCities[1])

        val state = viewModel.uiState.value
        assertEquals(SelectedPanel.Detail(sampleCities[1].id), state.selectedPanel)
    }

    @Test
    fun `clearSelection resets selectedCityId and panel to None`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onCitySelected(sampleCities[0])
        viewModel.clearSelection()

        val state = viewModel.uiState.value
        assertNull(state.selectedCityId)
        assertEquals(SelectedPanel.None, state.selectedPanel)
    }


/*
    @Test
    fun `onToggleFavoritesFilter toggles showOnlyFavorites flag`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.showOnlyFavorites)
        viewModel.onToggleFavoritesFilter()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.showOnlyFavorites)
    }
*/

    @Test
    fun `onToggleFavorite delegates to toggleFavoriteUseCase`() = runTest {
        viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onToggleFavorite(42)
        advanceUntilIdle()

        verify(toggleFavoriteUseCase).invoke(42)
    }

    @Test
    fun `uiState flow emits loading then cities via turbine`() = runTest {
        viewModel = buildViewModel()

        viewModel.uiState.test {

            val loading = awaitItem()
            assertTrue(loading.isLoading)

            advanceUntilIdle()

            val loaded = awaitItem()
            assertFalse(loaded.isLoading)
            assertEquals(sampleCities, loaded.cities)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun buildViewModel() = CityListViewModel(
        getCitiesUseCase = getCitiesUseCase,
        searchCitiesUseCase = searchCitiesUseCase,
        toggleFavoriteUseCase = toggleFavoriteUseCase,
        getFavoriteIdsUseCase = getFavoriteIdsUseCase,
    )

    private fun city(id: Int, name: String) = City(
        id = id,
        name = name,
        country = "US",
        lat = 0.0,
        lon = 0.0,
        normalizedName = normalize(name)
    )
}
