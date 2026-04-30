package com.app.cities.presentation.detail

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.cities.domain.model.City
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailScreen_displaysCityDetails() {
        val city = City(
            id = 1,
            name = "Buenos Aires",
            country = "AR",
            lat = -34.6037,
            lon = -58.3816,
            normalizedName = "buenos aires"
        )

        composeTestRule.setContent {
            CityDetailScreen(
                city = city,
                onBack = {}
            )
        }

        composeTestRule.onAllNodesWithText("Buenos Aires").assertCountEquals(2)
        composeTestRule.onNodeWithText("AR").assertExists()
        composeTestRule.onNodeWithText("-34.6037°, -58.3816°").assertExists()
    }

    @Test
    fun detailScreen_backButtonClicks() {
        val city = City(
            id = 1,
            name = "Buenos Aires",
            country = "AR",
            lat = -34.6037,
            lon = -58.3816,
            normalizedName = "buenos aires"
        )
        var backClicked = false

        composeTestRule.setContent {

            CityDetailScreen(
                city = city,
                onBack = { backClicked = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backClicked)

    }
}
