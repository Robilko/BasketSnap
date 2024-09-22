package ru.robilko.basket_snap

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class BasketSnapUiTests {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun open_league_details_screen(): Unit = with(rule) {
        waitUntil(timeoutMillis = 3000) {
            onAllNodesWithTag("HomeSearchField").fetchSemanticsNodes().isNotEmpty()
        }
        onNodeWithTag("HomeSearchField").apply {
            performClick()
            onChild().performTextInput("USA")
        }
        waitUntil(timeoutMillis = 3000) {
            onAllNodesWithText("USA").fetchSemanticsNodes().isNotEmpty()
        }
        onNode(hasTestTag("CountryCard") and hasText("USA")).performClick()
        waitUntil(timeoutMillis = 3000) {
            onAllNodesWithTag("LeaguesList").fetchSemanticsNodes().isNotEmpty()
        }
        onNodeWithTag("LeaguesList").performScrollToNode(hasText("NBA"))
        onNodeWithText("NBA").performClick()
        waitUntil(timeoutMillis = 3000) {
            onAllNodesWithTag("LeagueDetails").fetchSemanticsNodes().isNotEmpty()
        }
        onNode(hasTestTag("SeasonItemCard") and hasText("2023-2024")).performClick()
        waitUntil(timeoutMillis = 3000) {
            onAllNodesWithTag("SeasonDialog").fetchSemanticsNodes().isNotEmpty()
        }
        onNode(hasText("Teams") or hasText("Команды")).performClick()
        waitUntil(timeoutMillis = 3000) {
            onAllNodesWithTag("TeamsList").fetchSemanticsNodes().isNotEmpty()
        }

        onNodeWithTag("TeamsList").performScrollToNode(hasText("Los Angeles Lakers"))
        onNode(hasTestTag("TeamCard") and hasText("Los Angeles Lakers")).performClick()
        waitUntil(timeoutMillis = 3000) {
            onAllNodesWithTag("GamesList").fetchSemanticsNodes().isNotEmpty()
        }
        onNodeWithTag("GamesList").isDisplayed()
    }
}