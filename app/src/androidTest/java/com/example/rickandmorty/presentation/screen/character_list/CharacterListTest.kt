package com.example.rickandmorty.presentation.screen.character_list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rickandmorty.presentation.screen.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterListTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun characterList_displaysSearchField() {
        composeRule
            .onNodeWithText("Busque um personagem...")
            .assertIsDisplayed()
    }

    @Test
    fun characterList_clickCharacter_navigatesToDetail() {
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Rick Sanchez").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Rick Sanchez").performClick()

        composeRule.onAllNodesWithText("Origem").fetchSemanticsNodes().isNotEmpty()
        composeRule.onAllNodesWithText("Local Atual").fetchSemanticsNodes().isNotEmpty()
    }
}